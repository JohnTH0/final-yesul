package com.yesul.chatroom.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yesul.admin.model.dto.auth.LoginAdmin;
import com.yesul.chatroom.model.dto.request.MessageRequestDto;
import com.yesul.chatroom.model.dto.response.MessageResponseDto;
import com.yesul.chatroom.model.entity.enums.Type;
import com.yesul.chatroom.service.MessageService;
import com.yesul.notification.model.dto.request.CreateNotificationRequestDto;
import com.yesul.notification.model.entity.enums.NotificationType;
import com.yesul.notification.service.NotificationService;
import com.yesul.user.service.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChatSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final MessageService messageService;
    private final NotificationService notificationService;

    // 복합키 (userId_type) → WebSocketSession
    private final Map<String, WebSocketSession> sessions = new HashMap<>();

    private String makeKey(Long id, Type type) {
        return id + "_" + type.name();
    }

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
        Type type = (principalDetails != null) ? Type.USER : Type.ADMIN;
        String sessionKey = makeKey(userId, type);

        // 기존 세션 제거
        WebSocketSession existing = sessions.get(sessionKey);
        if (existing != null && existing.isOpen()) {
            try {
                existing.close();
            } catch (Exception e) {
                log.warn("기존 세션 종료 실패: {}", e.getMessage());
            }
        }

        sessions.put(sessionKey, session);
        log.info("WebSocket 연결 성공! userId={}, type={}, sessionId={}", userId, type.name(), session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        String payload = textMessage.getPayload();
        MessageRequestDto messageRequestDto = objectMapper.readValue(payload, MessageRequestDto.class);

        PrincipalDetails principalDetails = null;
        LoginAdmin loginAdmin = null;

        Object rawPrincipal = session.getPrincipal();
        if (rawPrincipal instanceof PrincipalDetails) {
            principalDetails = (PrincipalDetails) rawPrincipal;
        } else if (rawPrincipal instanceof LoginAdmin) {
            loginAdmin = (LoginAdmin) rawPrincipal;
        } else if (rawPrincipal instanceof OAuth2AuthenticationToken oauth2Token) {
            Object oauthPrincipal = oauth2Token.getPrincipal();
            if (oauthPrincipal instanceof PrincipalDetails) {
                principalDetails = (PrincipalDetails) oauthPrincipal;
            }
        } else if (rawPrincipal instanceof Authentication authentication) {
            Object authPrincipal = authentication.getPrincipal();
            if (authPrincipal instanceof PrincipalDetails) {
                principalDetails = (PrincipalDetails) authPrincipal;
            } else if (authPrincipal instanceof LoginAdmin) {
                loginAdmin = (LoginAdmin) authPrincipal;
            }
        }

        Long senderId = principalDetails != null ? principalDetails.getUser().getId() : loginAdmin.getId();
        Type senderType = (principalDetails != null) ? Type.USER : Type.ADMIN;
        messageRequestDto.setSenderType(senderType);

        MessageResponseDto messageResponseDto = messageService.saveMessage(messageRequestDto, senderId);
        String responseJson = objectMapper.writeValueAsString(messageResponseDto);

        // 수신자 세션에 전송
        String receiverKey = makeKey(messageResponseDto.getReceiverId(), messageResponseDto.getReceiverType());
        WebSocketSession receiverSession = sessions.get(receiverKey);
        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.sendMessage(new TextMessage(responseJson));
        } else {
            log.info("수신자 세션 없음: {}", receiverKey);
        }

        // 송신자 세션에 전송
        String senderKey = makeKey(senderId, senderType);
        WebSocketSession senderSession = sessions.get(senderKey);
        if (senderSession != null && senderSession.isOpen()) {
            senderSession.sendMessage(new TextMessage(responseJson));
        } else {
            log.info("송신자 세션 없음: {}", senderKey);
        }

        String content = messageResponseDto.getReceiverType() == Type.ADMIN
                ? "새로운 문의가 도착했습니다."
                : "관리자의 답변이 도착했습니다.";

        notificationService.sendNotification(CreateNotificationRequestDto.builder()
                .senderId(senderId)
                .senderType(senderType)
                .senderName(messageResponseDto.getSenderName())
                .receiverId(messageResponseDto.getReceiverId())
                .receiverType(messageResponseDto.getReceiverType())
                .targetId(messageRequestDto.getChatRoomId())
                .type(NotificationType.CHAT)
                .content(content)
                .build());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.entrySet().removeIf(entry -> entry.getValue().equals(session));
    }
}
