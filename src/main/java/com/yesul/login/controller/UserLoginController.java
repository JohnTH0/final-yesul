package com.yesul.login.controller;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.yesul.user.model.entity.User;
import com.yesul.user.model.dto.request.UserPasswordResetMailRequestDto;
import com.yesul.user.service.UserService;
import com.yesul.user.service.UserAsyncService;

@Tag(name = "사용자 로그인/비밀번호 초기화", description = "로그인 폼, 비밀번호 초기화 요청을 처리합니다.")
@Controller
@RequiredArgsConstructor
@Slf4j
public class UserLoginController {

    private final UserService userService;
    private final UserAsyncService userAsyncService;

    @Operation(summary="로그인 폼", description="이미 로그인되어 있지 않은 사용자의 로그인 화면을 반환합니다.")
    @GetMapping("/login")
    public String loginForm() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/";
        }
        return "login/login";
    }

    @Operation(summary="로그아웃 리디렉션", description="로그아웃 후 로그인 페이지로 이동합니다.")
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login/login";
    }

    @Operation(summary="비밀번호 초기화 메일 요청폼", description="비밀번호 초기화 메일 요청 화면을 보여줍니다.")
    @GetMapping("/reset-password")
    public String showRequestForm(Model model) {
        model.addAttribute("resetMailDto", UserPasswordResetMailRequestDto.builder().build());
        return "login/password-reset-request";
    }

    @Operation(summary="비밀번호 초기화 메일 발송", description="유효한 이메일에 한해 초기화 링크가 담긴 메일을 발송합니다.")
    @PostMapping("/reset-password")
    public String handleRequest(
            @Validated @ModelAttribute("resetMailDto") UserPasswordResetMailRequestDto dto,
            BindingResult br,
            RedirectAttributes ra) {

        if (br.hasErrors()) {
            return "login/password-reset-request";
        }

        Optional<User> checkUser = userService.findUserByEmail(dto.getEmail());

        if (checkUser.isEmpty()) {
            ra.addFlashAttribute("error", "등록되지 않은 메일입니다");
            return "redirect:/reset-password";
        }

        User user = checkUser.get();

        if (user.getStatus() != '1' && user.getStatus() != '2') {
            ra.addFlashAttribute("error", "현재 상태에서 요청을 처리할 수 없습니다.");
            return "redirect:/reset-password";
        }

        if (user.getStatus() == '1') {
            userAsyncService.resendPasswordResetLink(user);
        } else{
            userAsyncService.resendSignUpVerification(user);
        }

        String msg = (user.getStatus() == '1')
                ? "비밀번호 재설정 메일을 발송했습니다."
                : "가입 인증 메일을 재발송했습니다.";
        ra.addFlashAttribute("message", msg);
        return "redirect:/password-reset-complete";
    }

    @Operation(summary="비밀번호 초기화 완료 페이지", description="비밀번호 초기화 메일 발송 후 보여줄 완료 화면입니다.")
    @GetMapping("/password-reset-complete")
    public String showRequestComplete() {
        return "login/password-reset-complete";
    }
}