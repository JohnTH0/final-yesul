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
import com.yesul.user.model.entity.User;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAsyncService {
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

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
     * 회원가입 이메일 발송
     */
    @Async("asyncExecutor")
    public void sendVerificationEmailAsync(User user) {
        try {
            sendHtmlMail(
                    user.getEmail(),
                    "[Yesul] 회원가입 이메일 인증",
                    buildSignUpContent(user)
            );
            log.info("Async: signUpVerification sent to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Background registration failed for {}: {}", user.getEmail(), e.getMessage(), e);
        }
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

    private String buildSignUpContent(User user) {
        String link = "https://yesul.shop/user/verify-email?"
                + "email=" + user.getEmail()
                + "&token=" + user.getEmailCheckToken();
        return "<p>안녕하세요, " + user.getName() + "님!</p>"
                + "<p>아래 링크를 클릭해 회원가입 인증을 완료해주세요:</p>"
                + "<a href=\"" + link + "\">인증하기</a>"
                + "<p>이 링크는 15분간 유효합니다.</p>";
    }

    private String buildPasswordResetContent(User user) {
        String link = "https://yesul.shop/user/reset-new-password?"
                + "email=" + user.getEmail()
                + "&token=" + user.getEmailCheckToken();
        return "<p>안녕하세요, " + user.getName() + "님!</p>"
                + "<p>아래 링크를 클릭해 비밀번호를 재설정하세요:</p>"
                + "<a href=\"" + link + "\">비밀번호 재설정</a>"
                + "<p>이 링크는 15분간 유효합니다.</p>";
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
