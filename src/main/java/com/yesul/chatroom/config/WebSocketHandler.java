//package com.yesul.chatroom.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.yesul.chatroom.model.entity.Message;
//import com.yesul.chatroom.service.ChatRoomService;
//import java.util.HashMap;
//import java.util.Map;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class WebSocketHandler extends TextWebSocketHandler {
//    private final ObjectMapper objectMapper;
//    private final ChatRoomService chatRoomService;
//
//    private final Map<String, WebSocketSession> sessions = new HashMap<>();
//
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//
//    }
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
//        String payload = textMessage.getPayload();
//        Message message = objectMapper.readValue(payload, Message.class);
//
//        sessions.putIfAbsent(message.getSenderId(), session); //발신자 세션 등록
//
//        chatRoomService.saveMessage(message); //메시지 저장
//
//        //수신자에게 전달
//        WebSocketSession receiverSession = sessions.get(session.getId());
//
//
//
//    }
//
//
//    @Override //연결이 끝나면 호출
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
//        sessions.entrySet().removeIf(entry -> entry.getValue().equals(session));
//    }
//
//
//}
