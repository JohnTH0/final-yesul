package com.yesul.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice
public class GlobalExceptionAdvice {
//
//    @ExceptionHandler(EntityNotFoundException.class)
//    public String handleEntityNotFound(EntityNotFoundException e,HttpServletRequest request) {
//        // 로그 남기기
//        log.warn("[EntityNotFoundException] {} {} - {}", request.getMethod(), request.getRequestURI(), e.getMessage());
//        return "404.html";
//    }



}
