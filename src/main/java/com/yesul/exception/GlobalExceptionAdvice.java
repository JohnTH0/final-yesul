package com.yesul.exception;

import com.yesul.exception.handler.AdminNotFoundException;
import com.yesul.exception.handler.ChatRoomNotFoundException;
import com.yesul.exception.handler.NotificationNotFoundException;
import com.yesul.exception.handler.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(UserNotFoundException e, HttpServletRequest request) {
        // 로그 남기기
        log.warn("[UserNotFoundException] {} {} - {}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return "404.html";
    }

    @ExceptionHandler(ChatRoomNotFoundException.class)
    public String handleChatRoomNotFound(ChatRoomNotFoundException e, HttpServletRequest request) {
        log.warn("[ChatRoomNotFoundException] {} {} - {}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return "404.html";
    }
    @ExceptionHandler(AdminNotFoundException.class)
    public String handleAdminNotFound(AdminNotFoundException e, HttpServletRequest request) {
        log.warn("[AdminNotFoundException] {} {} - {}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return "404.html";
    }

    @ExceptionHandler(NotificationNotFoundException.class)
    public String NotificationNotFound(NotificationNotFoundException e, HttpServletRequest request) {
        log.warn("[NotificationNotFoundException] {} {} - {}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return "404.html";
    }



}
