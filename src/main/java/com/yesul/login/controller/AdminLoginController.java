package com.yesul.login.controller;

import com.yesul.login.service.AdminOtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminLoginController {

    private final AdminOtpService adminOtpService;

    @GetMapping("/login")
    public void loginPage(){}

    @GetMapping("/otp")
    public void otpLoginPage() {}

    @PostMapping("/otp/verify")
    public String verifyOtp(@RequestParam int otpCode,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {

        boolean success = adminOtpService.verifyOtpAndAuthenticate(otpCode, authentication);

        if (!success) {
            redirectAttributes.addFlashAttribute("message", "OTP 인증 실패하였습니다.");
            return "redirect:/admin/otp";
        }

        return "redirect:/admin/dashboard";
    }
}