package com.yesul.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    // 모든 예외를 하나로 처리
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model, HttpServletRequest request) {
        model.addAttribute("message", "서버 내부 오류가 발생했습니다.");
        model.addAttribute("detail", e.getMessage());
        model.addAttribute("path", request.getRequestURI());
        return "error/general-error"; // 이 뷰를 보여줌
    }
}
