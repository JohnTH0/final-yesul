package com.yesul.chatroom.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yesul.chatroom.model.dto.MessageRequestDto;
import com.yesul.chatroom.model.dto.MessageResponseDto;
import com.yesul.chatroom.service.MessageService;
import com.yesul.user.service.PrincipalDetails;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    //userId -> WebSocketSession
    private final Map<Long, WebSocketSession> sessions = new HashMap<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Object rawPrincipal = session.getPrincipal();
        PrincipalDetails principalDetails = null;

        if (rawPrincipal instanceof PrincipalDetails) {
            // 일반 로그인
            principalDetails = (PrincipalDetails) rawPrincipal;

        } else if (rawPrincipal instanceof OAuth2AuthenticationToken oauth2Token) {
            // OAuth2 로그인
            Object oauthPrincipal = oauth2Token.getPrincipal();
            if (oauthPrincipal instanceof PrincipalDetails) {
                principalDetails = (PrincipalDetails) oauthPrincipal;
            }
        }

        if (principalDetails == null) {
            log.warn("⚠️ 예상치 못한 Principal 구조: {}", rawPrincipal);
            session.close();
            return;
        }

        Long userId = principalDetails.getUser().getId();
        sessions.putIfAbsent(userId, session);
        log.info("✅ 웹소켓 연결 성공! userId={}", userId);
    }



    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {

        String payload = textMessage.getPayload();
        MessageRequestDto message = objectMapper.readValue(payload, MessageRequestDto.class);

        MessageResponseDto messageResponseDto = messageService.saveMessage(message, message.getUserId());

        // 수신자 세션 찾아서 전달
        WebSocketSession receiverSession = sessions.get(messageResponseDto.getReceiverId());
        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } else {
            System.out.println("수신자 없음");
            log.info(" 수신자 오프라인: {}", messageResponseDto.getReceiverId());
            // TODO: Push 알림 처리
        }
    }



    @Override //연결이 끝나면 호출
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.entrySet().removeIf(entry -> entry.getValue().equals(session));
    }
}
