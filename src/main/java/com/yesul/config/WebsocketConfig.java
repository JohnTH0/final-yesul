package com.yesul.config;

import com.yesul.chatroom.config.ChatSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@RequiredArgsConstructor
@Configuration
@EnableWebSocket  // websocket 서버로서 동작하겟다는 어노테이션
public class WebsocketConfig implements WebSocketConfigurer {

    private final ChatSocketHandler chatSocketHandler;
    private final com.yesul.notification.handler.NotificationHandler notificationHandler;


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatSocketHandler, "/ws/chat").setAllowedOrigins("*");
        // handler 등록,   js에서 new Websocket할 때 경로 지정
        //다른 url에서도 접속할 수있게(CORS설정)
        registry.addHandler(notificationHandler, "/ws/notification").setAllowedOrigins("*");
    }
}
