package com.yesul.user.service;

import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.yesul.user.model.entity.User;
import com.yesul.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordAsyncService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    /**
     * 가입 인증 메일 비동기 발송
     */
    @Async("asyncExecutor")
    public void sendSignUpVerificationMail(User user) {
        // 토큰 재생성
        user.generateEmailCheckToken();
        userRepository.save(user);

        String link = "http://localhost:8080/user/verify-email?email="
                + user.getEmail() + "&token=" + user.getEmailCheckToken();

        sendHtmlMail(
                user.getEmail(),
                "[Yesul] 회원가입 인증 안내",
                "<p>아래 링크로 인증을 완료하세요:</p>"
                        + "<a href=\"" + link + "\">인증하기</a>"
                        + "<p>15분간 유효합니다.</p>"
        );
        log.info("가입 인증 메일 비동기 발송: {}", user.getEmail());
    }

    /**
     * 비밀번호 재설정 메일 비동기 발송
     */
    @Async("asyncExecutor")
    public void sendPasswordResetMail(User user) {
        // 토큰 재생성
        user.generateEmailCheckToken();
        userRepository.save(user);

        String link = "http://localhost:8080/user/password-reset?email="
                + user.getEmail() + "&token=" + user.getEmailCheckToken();

        sendHtmlMail(
                user.getEmail(),
                "[Yesul] 비밀번호 재설정 안내",
                "<p>아래 링크를 클릭해 새 비밀번호를 설정하세요:</p>"
                        + "<a href=\"" + link + "\">비밀번호 재설정</a>"
                        + "<p>15분간 유효합니다.</p>"
        );
        log.info("비밀번호 재설정 메일 비동기 발송: {}", user.getEmail());
    }

    // 공통 HTML 메일 전송 메서드
    private void sendHtmlMail(String to, String subject, String htmlBody) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, false, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(msg);
        } catch (Exception e) {
            log.error("HTML 메일 발송 실패 to={} subject={}", to, subject, e);
        }
    }
}