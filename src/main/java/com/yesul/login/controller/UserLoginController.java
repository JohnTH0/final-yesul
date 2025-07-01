package com.yesul.login.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
public class UserLoginController {

    @GetMapping("/login")
    public String loginForm() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 로그인 확인
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/user/profile";
        }
        // 로그인 상태가 아닐 시
        return "/user/login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/user/login?logout";
    }
}