package com.yesul.user.controller;

import com.yesul.user.model.dto.*;
import com.yesul.user.model.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import com.yesul.exception.handler.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yesul.exception.handler.UserNotFoundException;
import com.yesul.user.service.PrincipalDetails;
import com.yesul.user.service.RegistrationAsyncService;
import com.yesul.user.service.UserService;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RegistrationAsyncService asyncRegService;
    private final PasswordEncoder passwordEncoder;

    // Regist Start
    // 회원가입 페이지 이동
    @GetMapping("/regist")
    public String registForm(Model model) {
        model.addAttribute("userRegisterDto", new UserRegisterDto());
        return "user/regist";
    }

    // 회원가입
    @PostMapping("/regist-process")
    public String registProcess(
            @Validated @ModelAttribute("userRegisterDto") UserRegisterDto dto,
            RedirectAttributes attr) {
        asyncRegService.registerInBackground(dto);
        attr.addFlashAttribute("message", "회원가입 요청을 접수했습니다. 잠시 후 이메일을 확인해주세요.");
        return "redirect:/user/user-regist-mail";
    }

    // 회원가입 후 메일 인증 페이지이동
    @GetMapping("/user-regist-mail")
    public String userRegistMail() {
        return "user/user-regist-mail";
    }

    // 이메일 인증
    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam String email, @RequestParam String token, RedirectAttributes redirectAttributes) {
        try {
            boolean verified = userService.verifyEmail(email, token);
            if (verified) {
                redirectAttributes.addFlashAttribute("message", "이메일 인증이 완료되었습니다! 이제 로그인해주세요.");
                log.info("이메일 인증 성공: {}", email);
                return "redirect:/login/";
            } else {
                redirectAttributes.addFlashAttribute("error", "이메일 인증에 실패했습니다. 링크가 유효하지 않거나 만료되어 새로운 인증 메일이 발송되었습니다. 다시 이메일을 확인해주세요.");
                log.warn("이메일 인증 실패 (토큰 문제): {}", email);
                return "redirect:/user/regist";
            }
        } catch (EntityNotFoundException e) {
            log.error("이메일 인증 실패: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "이메일 인증에 실패했습니다. 사용자 정보를 찾을 수 없습니다.");
            return "redirect:/user/regist";
        } catch (Exception e) {
            log.error("이메일 인증 중 예외 발생: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "이메일 인증 중 오류가 발생했습니다. 다시 시도해주세요.");
            return "redirect:/user/regist";
        }
    }
    // Regist End

    // Mypage Start
    @GetMapping("/profile")
    public String userProfile(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (principalDetails == null) {
            redirectAttributes.addFlashAttribute("error", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        String email = principalDetails.getUsername();
        userService.findUserByEmail(email)
                .ifPresentOrElse(
                        user -> model.addAttribute("user", user),
                        () -> {
                            redirectAttributes.addFlashAttribute("error", "사용자를 찾을 수 없습니다.");
                        }
                );

        // 3) 사용자 정보 페이지로 이동
        return "user/user-profile";
    }

    @GetMapping("/profile/edit")
    public String userProfileEdit(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            String username = authentication.getName();
            userService.findUserByEmail(username).ifPresent(user -> model.addAttribute("user", user));
        }
        return "user/user-edit";
    }

    @PostMapping("/profile/update")
    public String updateUserProfile(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Validated @ModelAttribute("user") UserUpdateDto userUpdateDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (principalDetails == null) {
            return "redirect:/user/login";
        }

        if (bindingResult.hasErrors()) {
            log.error("유효성 검사 오류: {}", bindingResult.getAllErrors());
            // 오류가 있으면 다시 폼 페이지로 돌아가 오류 메시지를 표시
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            redirectAttributes.addFlashAttribute("user", userUpdateDto);
            return "redirect:/user/profile/edit";
        }

        Long userId = principalDetails.getUser().getId();

        try {
            userService.updateUserProfile(userId, userUpdateDto);
            redirectAttributes.addFlashAttribute("message", "프로필이 성공적으로 업데이트되었습니다.");
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/user/profile/edit";
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("newPassword", "passwordMismatch", e.getMessage());
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            redirectAttributes.addFlashAttribute("user", userUpdateDto);
            return "redirect:/user/profile/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "프로필 업데이트 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/user/profile/edit";
        }
        return "redirect:/user/profile";
    }

    @GetMapping("/reset-password")
    public String resetPasswordForm(Model model) {
        model.addAttribute("passwordResetDto", new UserPasswordResetDto());
        return "user/reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPasswordProcess(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Validated @ModelAttribute("passwordResetDto") UserPasswordResetDto dto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        // 로그인 체크
        if (principalDetails == null) {
            redirectAttributes.addFlashAttribute("error", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        // 폼 유효성 검사
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.passwordResetDto",
                    bindingResult);
            redirectAttributes.addFlashAttribute("passwordResetDto", dto);
            return "redirect:/user/reset-password";
        }

        // 1) 기존 비밀번호 일치 확인
        if (!passwordEncoder.matches(dto.getCurrentPassword(), principalDetails.getPassword())) {
            bindingResult.rejectValue("currentPassword", "wrongPassword", "현재 비밀번호가 올바르지 않습니다.");
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.passwordResetDto",
                    bindingResult);
            redirectAttributes.addFlashAttribute("passwordResetDto", dto);
            return "redirect:/user/reset-password";
        }

        // 2) 새 비밀번호로 업데이트
        try {
            userService.changePassword(principalDetails.getUser().getId(), dto.getNewPassword());
            redirectAttributes.addFlashAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "비밀번호 변경 중 오류가 발생했습니다.");
            return "redirect:/user/reset-password";
        }

        return "redirect:/user/profile";
    }

    @GetMapping("/resign")
    public String resignForm(Model model) {
        model.addAttribute("userResignDto", new UserResignDto());
        return "user/resign";
    }

    // 2) POST: 탈퇴 처리
    @PostMapping("/resign")
    public String resignProcess(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Validated @ModelAttribute("userResignDto") UserResignDto dto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (principalDetails == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        // 폼 검증
        if (bindingResult.hasErrors()) {
            return "user/resign";
        }

        try {
            // Service에서 비밀번호 확인 및 type 변경
            userService.resignUser(
                    principalDetails.getUser().getId(),
                    dto.getCurrentPassword()
            );
            // 정상 탈퇴하면 Spring Security 로그아웃 경로로 리다이렉트
            return "redirect:/logout";
        } catch (IllegalArgumentException e) {
            // 비밀번호 불일치 등
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/user/resign";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "탈퇴 중 오류가 발생했습니다.");
            return "redirect:/user/resign";
        }
    }

    @GetMapping("/password-request")
    public String showRequestForm(Model model) {
        model.addAttribute("requestDto", new UserPasswordRequestDto());
        return "user/password-request";
    }

    // 2) 요청 처리
    @PostMapping("/password-request")
    public String handleRequest(
            @Valid @ModelAttribute("requestDto") UserPasswordRequestDto dto,
            BindingResult br,
            RedirectAttributes ra) {

        if (br.hasErrors()) return "user/password-request";

        userService.findUserByEmail(dto.getEmail())
                .ifPresentOrElse(user -> {
                    if (user.getType() == '2') {
                        userService.resendSignUpVerification(dto.getEmail());
                        ra.addFlashAttribute("message", "가입 인증 메일을 재발송했습니다.");
                    } else if (user.getType() == '1') {
                        userService.resendPasswordResetLink(dto.getEmail());
                        ra.addFlashAttribute("message", "비밀번호 재설정 메일을 발송했습니다.");
                    } else {
                        ra.addFlashAttribute("message", "현재 상태에서 요청을 처리할 수 없습니다.");
                    }
                }, () -> {
                    ra.addFlashAttribute("message", "등록되지 않은 이메일입니다.");
                });

        return "redirect:/user/password-request-complete";
    }

    @GetMapping("/password-request-complete")
    public String showRequestComplete() {
        return "user/password-request-complete";
    }

    // 3) 비밀번호 재설정 폼
    @GetMapping("/password-reset")
    public String showResetForm(
            @RequestParam String email,
            @RequestParam String token,
            Model model,
            RedirectAttributes ra) {

        if (!userService.isPasswordResetTokenValid(email, token)) {
            ra.addFlashAttribute("message", "유효하지 않거나 만료된 링크입니다.");
            return "redirect:/user/password-request";
        }

        model.addAttribute("email", email);
        model.addAttribute("token", token);
        model.addAttribute("resetDto", new UserPasswordResetDto());
        return "user/password-reset";
    }

    // 4) 비밀번호 재설정 처리
    @PostMapping("/password-reset")
    public String handleReset(
            @RequestParam String email,
            @RequestParam String token,
            @Valid @ModelAttribute("resetDto") UserPasswordResetDto dto,
            BindingResult br,
            RedirectAttributes ra) {

        if (br.hasErrors()) {
            ra.addFlashAttribute("message", "비밀번호는 8~30자 사이여야 합니다.");
            return "redirect:/user/password-reset?email=" + email + "&token=" + token;
        }

        try {
            userService.resetPassword(email, token, dto.getNewPassword());
            ra.addFlashAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:/user/password-reset?email=" + email + "&token=" + token;
        }
    }
}