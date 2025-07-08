package com.yesul.user.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.yesul.user.repository.UserRepository;
import com.yesul.user.model.dto.UserRegisterDto;
import com.yesul.user.model.entity.User;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAsyncService {
    private final UserService userService;
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    /**
     * 회원가입 처리 전체를 비동기로 수행
     */
    @Async("asyncExecutor")
    public void registerInBackground(UserRegisterDto dto) {
        try {
            userService.registerUser(dto);
            log.info("Background registration finished for {}", dto.getEmail());
        } catch (Exception e) {
            log.error("Background registration failed for {}: {}", dto.getEmail(), e.getMessage(), e);
        }
    }

    /**
     * 2) 가입 인증 메일을 비동기로 재발송
     */
    @Async("asyncExecutor")
    public void resendSignUpVerification(User user) {
        user.generateEmailCheckToken();
        userRepository.save(user);
        sendHtmlMail(
                user.getEmail(),
                "[Yesul] 회원가입 인증 안내",
                buildSignUpContent(user)
        );
        log.info("Async: signUpVerification re-sent to {}", user.getEmail());
    }

    /**
     * 3) 비밀번호 재설정 링크 메일을 비동기로 발송
     */
    @Async("asyncExecutor")
    public void resendPasswordResetLink(User user) {
        user.generateEmailCheckToken();
        userRepository.save(user);
        sendHtmlMail(
                user.getEmail(),
                "[Yesul] 비밀번호 재설정 안내",
                buildPasswordResetContent(user)
        );
        log.info("Async: password reset link sent to {}", user.getEmail());
    }

    // ─────────────────────────────────────────────────────────────
    // 내부 헬퍼 메서드
    // ─────────────────────────────────────────────────────────────

    private void sendSignUpVerificationMail(User user) {
        // 메서드명은 같아도, 실제 호출 시기는 registerInBackground 안에서만
        sendHtmlMail(
                user.getEmail(),
                "[Yesul] 회원가입 이메일 인증을 완료해주세요.",
                buildSignUpContent(user)
        );
    }

    private String buildSignUpContent(User user) {
        String link = "http://localhost:8080/user/verify-email?"
                + "email=" + user.getEmail()
                + "&token=" + user.getEmailCheckToken();
        return "<p>안녕하세요, " + user.getName() + "님!</p>"
                + "<p>아래 링크를 클릭해 회원가입 인증을 완료해주세요:</p>"
                + "<a href=\"" + link + "\">인증하기</a>"
                + "<p>15분간 유효합니다.</p>";
    }

    private String buildPasswordResetContent(User user) {
        String link = "http://localhost:8080/password-reset?"
                + "email=" + user.getEmail()
                + "&token=" + user.getEmailCheckToken();
        return "<p>안녕하세요, " + user.getName() + "님!</p>"
                + "<p>아래 링크를 클릭해 비밀번호를 재설정하세요:</p>"
                + "<a href=\"" + link + "\">비밀번호 재설정</a>"
                + "<p>15분간 유효합니다.</p>";
    }

    private void sendHtmlMail(String to, String subject, String htmlBody) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, false, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(msg);
        } catch (MessagingException e) {
            log.error("HTML 메일 발송 실패 to={} subject={}", to, subject, e);
        }
    }
}
