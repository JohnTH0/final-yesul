package com.yesul.chatroom.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yesul.admin.model.dto.auth.LoginAdmin;
import com.yesul.chatroom.model.dto.MessageRequestDto;
import com.yesul.chatroom.model.dto.MessageResponseDto;
import com.yesul.chatroom.model.entity.enums.Type;
import com.yesul.chatroom.service.MessageService;
import com.yesul.user.service.PrincipalDetails;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChatSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final MessageService messageService;
    // userId -> WebSocketSession
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

        if (principalDetails == null && loginAdmin == null) {
            session.close();
            return;
        }

        Long userId = principalDetails != null ? principalDetails.getUser().getId() : loginAdmin.getId();
        sessions.putIfAbsent(userId, session);
        log.info("WebSocket 연결 성공! userId={}, sessionId={}", userId, session.getId());
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

        if (principalDetails == null && loginAdmin == null) {
            session.close();
            return;
        }

        Long senderId = principalDetails != null
                ? principalDetails.getUser().getId()
                : loginAdmin.getId();

        // senderType을 인증 정보로 강제 세팅
        if (principalDetails != null) {
            messageRequestDto.setSenderType(Type.USER);
        } else if (loginAdmin != null) {
            messageRequestDto.setSenderType(Type.ADMIN);
        }

        // 서비스에 senderType이 담긴 DTO 전달
        MessageResponseDto messageResponseDto = messageService.saveMessage(messageRequestDto, senderId);

        String responseJson = objectMapper.writeValueAsString(messageResponseDto);

        // 수신자에게
        WebSocketSession receiverSession = sessions.get(messageResponseDto.getReceiverId());
        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.sendMessage(new TextMessage(responseJson));
        } else {
            log.info("수신자 세션 없음: {}", messageResponseDto.getReceiverId());
        }

        // 송신자에게
        WebSocketSession senderSession = sessions.get(senderId);
        if (senderSession != null && senderSession.isOpen()) {
            senderSession.sendMessage(new TextMessage(responseJson)); //프론트에 push함
        } else {
            log.info("송신자 세션 없음: {}", senderId);
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.entrySet().removeIf(entry -> entry.getValue().equals(session));
    }

}
