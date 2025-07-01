package com.yesul.user.service;

import java.time.LocalDateTime;
import java.util.Optional;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.yesul.exception.handler.EntityNotFoundException;
import com.yesul.user.model.dto.UserRegisterDto;
import com.yesul.user.model.entity.User;
import com.yesul.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    /**
     * 일반 사용자 회원가입 처리 (이메일 인증 대기 상태로 저장 및 인증 메일 발송)
     *
     * @param userRegisterDto 회원가입 요청 DTO
     * @return 저장된 User 엔티티 (이메일 발송 등에 활용)
     * @throws IllegalArgumentException 중복된 이메일 또는 닉네임이 있을 경우
     */
    @Override
    @Transactional // 메서드 실행 중 예외 발생 시 롤백 처리
    public User registerUser(UserRegisterDto userRegisterDto) {
        // 1. 이메일 중복 확인 (기존 isMailDuplicated 역할)
        if (isEmailDuplicated(userRegisterDto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        // 2. 닉네임 중복 확인 (새로 추가)
        if (isNicknameDuplicated(userRegisterDto.getNickname())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        // 3. User 엔티티 생성 및 초기 설정
        User user = userRegisterDto.toEntity(passwordEncoder.encode(userRegisterDto.getPassword()));
        user.setStatus('2');
        user.generateEmailCheckToken();

        // 4. 사용자 정보 저장
        User savedUser = userRepository.save(user);

        // 5. 이메일 인증 메일 발송
        sendVerificationEmail(savedUser);

        log.info("새 사용자 등록 및 인증 이메일 발송 대기: {}", savedUser.getEmail());
        return savedUser;
    }

    /**
     * 이메일 중복 확인
     *
     * @param email 확인할 이메일
     * @return 중복되면 true, 아니면 false
     */
    @Override
    public boolean isEmailDuplicated(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    /**
     * 닉네임 중복 확인
     *
     * @param nickname 확인할 닉네임
     * @return 중복되면 true, 아니면 false
     */
    @Override
    public boolean isNicknameDuplicated(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }

    /**
     * 이메일 인증 메일 발송
     *
     * @param user 인증 메일을 받을 사용자 엔티티
     */
    @Override
    public void sendVerificationEmail(User user) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            // true는 멀티파트 메시지 허용 (파일 첨부 등), UTF-8 인코딩
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(user.getEmail()); // 수신자 이메일 주소
            mimeMessageHelper.setSubject("Yesul 회원가입 이메일 인증을 완료해주세요."); // 이메일 제목

            // 인증 링크 생성: 서버 주소와 인증 엔드포인트, 사용자 이메일, 토큰 포함
            String verificationLink = "http://localhost:8080/user/verify-email?email=" + user.getEmail() + "&token=" + user.getEmailCheckToken();
            String content = "안녕하세요, " + user.getName() + "님!<br><br>"
                    + "Yesul에 가입해 주셔서 감사합니다. 아래 링크를 클릭하여 회원가입을 완료해주세요: <br><br>"
                    + "<a href=\"" + verificationLink + "\">Yesul 회원가입 완료하기</a>"
                    + "<br><br>이 링크는 <strong>15분</strong> 동안 유효합니다. 이메일 인증을 완료하시면 Yesul의 모든 서비스를 이용할 수 있습니다."
                    + "<br><br>감사합니다.<br>Yesul 드림";
            mimeMessageHelper.setText(content, true); // true는 HTML 형식으로 전송
            javaMailSender.send(mimeMessage);
            log.info("인증 이메일 발송 성공: {}", user.getEmail());
        } catch (MessagingException e) {
            log.error("인증 이메일 발송 실패: {}", user.getEmail(), e);
            throw new RuntimeException("이메일 발송 중 오류가 발생했습니다. 다시 시도해주세요.", e);
        }
    }

    /**
     * 이메일 인증 링크를 통한 사용자 활성화 처리
     *
     * @param email 인증을 요청한 사용자의 이메일
     * @param token 이메일로 발송된 인증 토큰
     * @return 인증 성공 시 true, 실패 시 false
     */
    @Override
    @Transactional
    public boolean verifyEmail(String email, String token) {
        // 1. 이메일로 사용자 조회
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            log.warn("이메일 인증 실패: 해당 이메일을 가진 사용자를 찾을 수 없습니다. 이메: {}", email);
            throw new EntityNotFoundException("이메일 인증 실패: 사용자 정보를 찾을 수 없습니다.");
        }

        User user = optionalUser.get();

        // 2. 이미 인증된 계정인지 확인
        if (user.getStatus() == '1') {
            log.info("이메일 인증 성공 (이미 활성화된 계정): {}", email);
            return true;
        }

        // 3. 토큰 유효성 및 만료 시간 검사
        if (user.getEmailCheckToken() == null || !user.getEmailCheckToken().equals(token) ||
                user.getEmailCheckTokenGeneratedAt() == null ||
                user.getEmailCheckTokenGeneratedAt().plusMinutes(15).isBefore(LocalDateTime.now())) {
            
            // 만료 시 토큰 재발급
            log.warn("이메일 인증 실패: 유효하지 않거나 만료된 토큰입니다. 이메일: {}", email);
            user.generateEmailCheckToken();
            user.setStatus('2');
            userRepository.save(user);
            try {
                sendVerificationEmail(user);
                log.info("만료된 토큰으로 인한 인증 실패, 새로운 인증 이메일 재발송 완료: {}", user.getEmail());
            } catch (RuntimeException e) {
                log.error("만료된 토큰으로 인한 재발송 이메일 발송 실패: {}", user.getEmail(), e);
            }
            return false;
        }

        user.completeSignUp();
        userRepository.save(user);

        log.info("이메일 인증 완료 및 계정 활성화: {}", user.getEmail());
        return true;
    }

    /**
     * 이메일로 사용자 조회
     *
     * @param email 사용자 이메일
     * @return User 엔티티 (Optional)
     */
    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}