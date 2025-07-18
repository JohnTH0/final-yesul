package com.yesul.notification.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yesul.admin.model.dto.auth.LoginAdmin;
import com.yesul.notification.model.dto.response.NotificationResponseDto;
import com.yesul.user.service.PrincipalDetails;
import com.yesul.chatroom.model.entity.enums.Type;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;

    // 복합키: "userId_TYPE" -> WebSocketSession
    private final Map<String, WebSocketSession> sessions = new HashMap<>();

    private String makeKey(Long id, Type type) {
        return id + "_" + type.name();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
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

        Long id = principalDetails != null ? principalDetails.getUser().getId() : loginAdmin.getId();
        Type type = principalDetails != null ? Type.USER : Type.ADMIN;
        String sessionKey = makeKey(id, type);

        // 기존 세션 종료
        WebSocketSession existing = sessions.get(sessionKey);
        if (existing != null && existing.isOpen()) {
            try {
                existing.close();
            } catch (Exception e) {
                log.warn("기존 알림 세션 종료 실패: {}", e.getMessage());
            }
        }

        sessions.put(sessionKey, session);
        log.info("알림 WebSocket 연결 성공! userId={}, type={}, sessionId={}", id, type.name(), session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 알림은 클라이언트에서 메시지를 보낼 일이 없으므로 무시
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.entrySet().removeIf(entry -> entry.getValue().equals(session));
    }

    /**
     * 특정 사용자에게 알림 보내기
     */
    public void sendNotification(Long receiverId, Type receiverType, NotificationResponseDto notificationResponseDto) {
        String key = makeKey(receiverId, receiverType);
        WebSocketSession receiverSession = sessions.get(key);

        if (receiverSession != null && receiverSession.isOpen()) {
            try {
                String json = objectMapper.writeValueAsString(notificationResponseDto);
                receiverSession.sendMessage(new TextMessage(json));
                log.info("알림 전송 완료: receiverId={}, type={}", receiverId, receiverType);
            } catch (Exception e) {
                log.error("알림 전송 실패", e);
            }
        } else {
            log.info("수신자 알림 세션 없음: receiverId={}, type={}", receiverId, receiverType);
        }
    }
}
