package com.yesul.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yesul.exception.handler.EntityNotFoundException;
import com.yesul.exception.handler.UserNotFoundException;
import com.yesul.user.service.PrincipalDetails;
import com.yesul.user.service.UserAsyncService;
import com.yesul.user.service.UserService;
import com.yesul.user.model.entity.User;
import com.yesul.user.model.dto.*;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserAsyncService asyncRegService;
    private final PasswordEncoder passwordEncoder;

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

    // 유저 정보 페이지
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
        return "user/user-profile";
    }

    // 유저 정보 수정 페이지
    @GetMapping("/profile/edit")
    public String userProfileEdit(Model model,
                                  @AuthenticationPrincipal PrincipalDetails principalDetails,
                                  RedirectAttributes redirectAttributes) {

        if (principalDetails == null) {
            redirectAttributes.addFlashAttribute("error", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        User currentUser = principalDetails.getUser();

        UserUpdateDto dto = new UserUpdateDto();
        dto.setName(currentUser.getName());
        dto.setNickname(currentUser.getNickname());
        dto.setBirthday(currentUser.getBirthday());
        dto.setAddress(currentUser.getAddress());
        dto.setEmail(currentUser.getEmail());
        dto.setProfile(currentUser.getProfile());

        model.addAttribute("user", dto);

        return "user/user-edit";
    }

    // 유저정보 수정
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
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            redirectAttributes.addFlashAttribute("user", userUpdateDto);
            return "redirect:/user/profile/edit";
        }

        Long userId = principalDetails.getUser().getId();

        try {
            userService.updateUserProfile(userId, userUpdateDto);
            User updatedUser = userService.findUserByEmail(principalDetails.getUser().getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));
            principalDetails.setUser(updatedUser);

            redirectAttributes.addFlashAttribute("success", "회원 정보가 수정되었습니다.");
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

    // 비밀번호 변경 페이지 이동
    @GetMapping("/change-password")
    public String changePasswordForm(Model model) {
        model.addAttribute("passwordChangeDto", new UserPasswordChangeDto());
        return "user/change-password";
    }

    // 비밀번호 변경
    @PostMapping("/change-password")
    public String changePasswordProcess(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Validated @ModelAttribute("passwordChangeDto") UserPasswordChangeDto dto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (principalDetails == null) {
            redirectAttributes.addFlashAttribute("error", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.passwordResetDto",
                    bindingResult);
            redirectAttributes.addFlashAttribute("passwordResetDto", dto);
            return "redirect:/user/reset-password";
        }

        if (!passwordEncoder.matches(dto.getCurrentPassword(), principalDetails.getPassword())) {
            bindingResult.rejectValue("currentPassword", "wrongPassword", "현재 비밀번호가 올바르지 않습니다.");
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.passwordResetDto",
                    bindingResult);
            redirectAttributes.addFlashAttribute("passwordResetDto", dto);
            return "redirect:/user/reset-password";
        }

        try {
            userService.changePassword(principalDetails.getUser().getId(), dto.getNewPassword());
            redirectAttributes.addFlashAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "비밀번호 변경 중 오류가 발생했습니다.");
            return "redirect:/user/reset-password";
        }

        return "redirect:/user/profile";
    }

    // 회원 탈퇴 페이지 이동
    @GetMapping("/resign")
    public String resignForm(Model model) {
        model.addAttribute("userResignDto", new UserResignDto());
        return "user/resign";
    }

    // 회원 탈퇴
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

    // 4) 비밀번호 재설정
    @PostMapping("/password-reset")
    public String handleReset(
            @RequestParam String email,
            @RequestParam String token,
            @Validated @ModelAttribute("resetDto") UserPasswordChangeDto dto,
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