package com.yesul.user.service;

import java.time.LocalDateTime;
import java.util.Optional;

import com.yesul.exception.handler.UnauthorizedException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.yesul.user.model.entity.User;
import com.yesul.user.model.dto.request.UserUpdateRequestDto;
import com.yesul.user.model.dto.request.UserRegisterRequestDto;
import com.yesul.user.repository.UserRepository;
import com.yesul.exception.handler.UserNotFoundException;
import com.yesul.exception.handler.DuplicateException;
import com.yesul.utill.ImageUpload;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageUpload imageUpload;


    /**
     * 일반 사용자 회원가입 처리 (이메일 인증 대기 상태로 저장 및 인증 메일 발송)
     *
     * @param userRegisterRequestDto 회원가입 요청 DTO
     * @return 저장된 User 엔티티 (이메일 발송 등에 활용)
     * @throws IllegalArgumentException 중복된 이메일 또는 닉네임이 있을 경우
     */
    @Override
    @Transactional
    public User registerUser(UserRegisterRequestDto userRegisterRequestDto) {
        if (isEmailDuplicated(userRegisterRequestDto.getEmail())) {
            throw new DuplicateException("이미 존재하는 이메일입니다.");
        }
        if (isNicknameDuplicated(userRegisterRequestDto.getNickname())) {
            throw new DuplicateException("이미 존재하는 닉네임입니다.");
        }

        // DTO → Entity
        User user = userRegisterRequestDto.toEntity(passwordEncoder.encode(userRegisterRequestDto.getPassword()));
        user.generateEmailCheckToken();
        return userRepository.save(user);

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
     * 이메일 인증 링크를 통한 사용자 활성화 처리
     *
     * @param email 인증을 요청한 사용자의 이메일
     * @param token 이메일로 발송된 인증 토큰
     * @return 인증 성공 시 true, 실패 시 false
     */
    @Override
    @Transactional
    public boolean verifyEmail(String email, String token) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            log.warn("이메일 인증 실패: 해당 이메일을 가진 사용자를 찾을 수 없습니다. 이메일: {}", email);
            throw new UserNotFoundException("이메일 인증 실패: 사용자 정보를 찾을 수 없습니다.");
        }

        User user = optionalUser.get();

        if (user.getStatus() == '1') {
            log.info("이메일 인증 성공 (이미 활성화된 계정): {}", email);
            return true;
        }

        if (user.getEmailCheckToken() == null || !user.getEmailCheckToken().equals(token) ||
                user.getEmailCheckTokenGeneratedAt() == null ||
                user.getEmailCheckTokenGeneratedAt().plusMinutes(15).isBefore(LocalDateTime.now())) {
            
            log.warn("이메일 인증 실패: 유효하지 않거나 만료된 토큰입니다. 이메일: {}", email);
            user.refreshEmailCheckTokenAndMarkUnverified();
            userRepository.save(user);
            return false;
        }

        user.completeSignUp();
        userRepository.save(user);

        log.info("이메일 인증 완료 및 계정 활성화: {}", user.getEmail());
        return true;
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public User updateUserProfile(Long userId, UserUpdateRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        if (dto.getName() != null)     user.updateName(dto.getName());
        if (dto.getNickname() != null) user.updateNickname(dto.getNickname());
        if (dto.getBirthday() != null) user.updateBirthday(dto.getBirthday());
        if (dto.getAddress() != null)  user.updateAddress(dto.getAddress());

        if (dto.getProfileImage() != null && !dto.getProfileImage().isEmpty()) {
            String url = imageUpload.uploadAndGetUrl("profile", dto.getProfileImage());
            user.updateProfileUrl(url);
        }

        return user;
    }

    @Override
    @Transactional
    public void changePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        String encoded = passwordEncoder.encode(newPassword);
        user.updatePassword(encoded);

        log.info("비밀번호 변경 완료: userId={}", userId);
    }

    @Override
    @Transactional
    public void resignUser(Long userId, String rawPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
        }

        user.markAsResigned();

        log.info("사용자 탈퇴 처리 완료: ID {}", userId);
    }

    @Override
    @Transactional
    public void resetPassword(String email, String token, String newPassword) {
        User user = userRepository.findByEmail(email)
                .filter(u -> token.equals(u.getEmailCheckToken()))
                .orElseThrow(() -> new UnauthorizedException("유효하지 않거나 만료된 링크입니다."));

        if (user.getEmailCheckTokenGeneratedAt().plusMinutes(15).isBefore(LocalDateTime.now())) {
            throw new UnauthorizedException("비밀번호 재설정 링크가 만료되었습니다.");
        }

        String encoded = passwordEncoder.encode(newPassword);
        user.updatePassword(encoded);

        user.clearEmailCheckToken();

        log.info("비밀번호 재설정 완료: {}", email);
    }
}