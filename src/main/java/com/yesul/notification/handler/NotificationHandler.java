package com.yesul.notification.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yesul.admin.model.dto.auth.LoginAdmin;
import com.yesul.notification.model.dto.response.NotificationResponseDto;
import com.yesul.user.service.PrincipalDetails;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;

    // userId -> 알림용 WebSocketSession
    private final Map<Long, WebSocketSession> sessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Object rawPrincipal = session.getPrincipal();

        PrincipalDetails principalDetails = null;
        LoginAdmin loginAdmin = null;

        if (rawPrincipal instanceof PrincipalDetails) {
            principalDetails = (PrincipalDetails) rawPrincipal;
        } else if (rawPrincipal instanceof LoginAdmin) {
            loginAdmin = (LoginAdmin) rawPrincipal;
        } else if (rawPrincipal instanceof Authentication authentication) {
            Object principalObj = authentication.getPrincipal();
            if (principalObj instanceof PrincipalDetails) {
                principalDetails = (PrincipalDetails) principalObj;
            } else if (principalObj instanceof LoginAdmin) {
                loginAdmin = (LoginAdmin) principalObj;
            }
        }

        Long userId = principalDetails != null ? principalDetails.getUser().getId() : loginAdmin.getId();
        sessions.putIfAbsent(userId, session);
        log.info("알림 WebSocket 연결 성공! userId={}, sessionId={}", userId, session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.entrySet().removeIf(entry -> entry.getValue().equals(session));
    }

    /**
     * 특정 사용자에게 알림 보내기
     */
    public void sendNotification(Long receiverId, NotificationResponseDto notificationResponseDto) {
        WebSocketSession receiverSession = sessions.get(receiverId);
        if (receiverSession != null && receiverSession.isOpen()) {
            try {
                String json = objectMapper.writeValueAsString(notificationResponseDto);
                receiverSession.sendMessage(new TextMessage(json));
                log.info("알림 전송 완료: receiverId={}", receiverId);
            } catch (Exception e) {
                log.error("알림 전송 실패", e);
            }
        } else {
            log.info("수신자 알림 세션 없음: {}", receiverId);
        }
    }
}

