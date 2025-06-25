package com.yesul.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@RequestMapping("/login")
public class UserLoginController {

    @GetMapping("/")
    public String loginForm() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {
            return "redirect:/";
        }
        return "user/login";
    }

    @PostMapping("/login-process")
    public String loginProcess() {
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/user/login?logout";
    }
}