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

        if (auth != null
                && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/main";
        }
        return "/user/login";
    }

//    @PostMapping("/login-process")
//    public String loginProcess() {
//        return "redirect:/";
//    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/user/login?logout";
    }
}