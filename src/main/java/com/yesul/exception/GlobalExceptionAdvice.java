package com.yesul.exception;

import com.yesul.exception.handler.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(UserNotFoundException e, HttpServletRequest request) {
        log.warn("[UserNotFoundException] {} {} - {}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return "404.html";
    }

    @ExceptionHandler(AlcoholNotFoundException.class)
    public String handleAlcoholNotFound(AlcoholNotFoundException e, HttpServletRequest request) {
        log.warn("[AlcoholNotFouneException] {} {} - {}", request.getMethod(), request.getRequestURI(), e.getMessage());
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

    @ExceptionHandler(DuplicateException.class)
    public String handleConflict(DuplicateException e, HttpServletRequest req) {
        log.warn("[409 DuplicateException] {} {} - {}", req.getMethod(), req.getRequestURI(), e.getMessage());
        return "409";
    }

    @ExceptionHandler(UnauthorizedException.class)
    public String handleUnauthorized(UnauthorizedException e, HttpServletRequest req) {
        log.warn("[401 UnauthorizedException] {} {} - {}", req.getMethod(), req.getRequestURI(), e.getMessage());
        return "401";
    }

    @ExceptionHandler(Exception.class)
    public String handleServerError(Exception e, HttpServletRequest req) {
        log.error("[500] {} {} - {}", req.getMethod(), req.getRequestURI(), e.getMessage(), e);
        return "500";
    }
}
