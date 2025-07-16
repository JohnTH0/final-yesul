package com.yesul.user.controller;

import com.yesul.like.model.dto.AlcoholLikeDto;
import com.yesul.like.service.AlcoholLikeService;
import com.yesul.user.model.dto.request.*;
import com.yesul.user.model.dto.response.UserProfileResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;

import com.yesul.like.model.dto.PostLikeDto;
import com.yesul.like.service.PostLikeService;
import com.yesul.exception.handler.EntityNotFoundException;
import com.yesul.user.service.PrincipalDetails;
import com.yesul.user.service.UserAsyncService;
import com.yesul.user.service.UserService;
import com.yesul.user.model.entity.User;

import java.util.List;

@Tag(name="사용자 관리 API", description="회원가입, 프로필, 비밀번호 변경, 탈퇴 등 사용자 관련 기능")
@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserAsyncService asyncRegService;
    private final PasswordEncoder passwordEncoder;
    private final PostLikeService likeService;
    private final AlcoholLikeService alcoholLikeService;

    @Operation(summary="회원가입 폼", description="새 사용자 가입 폼을 반환합니다.")
    @GetMapping("/regist")
    public String registForm(Model model) {
        model.addAttribute("userRegisterRequestDto", UserRegisterRequestDto.builder().build());
        return "user/regist";
    }

    @Operation(summary="회원가입 처리", description="유효성 검사 후 사용자 등록 및 인증 메일 발송")
    @ApiResponses({
            @ApiResponse(responseCode="200", description="가입 요청 접수"),
            @ApiResponse(responseCode="400", description="입력값 오류")
    })
    @PostMapping("/regist-process")
    public String registProcess(
            @Validated @ModelAttribute("userRegisterRequestDto") UserRegisterRequestDto dto,
            BindingResult br,
            RedirectAttributes attr,
            Model model) {

        if (br.hasErrors()) {
            return "user/regist";
        }

        User user = userService.registerUser(dto);

        asyncRegService.sendVerificationEmailAsync(user);

        String email = user.getEmail();
        String domain = email.substring(email.indexOf('@') + 1);
        model.addAttribute("mailDomainUrl", "https://" + domain);
        attr.addFlashAttribute("message", "회원가입 요청을 접수했습니다. 잠시 후 이메일을 확인해주세요.");

        return "user/user-regist-mail";
    }

    @Operation(summary="이메일 인증", description="메일 링크로 받은 이메일/토큰으로 계정 활성화")
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

    @Operation(summary="프로필 조회", description="로그인한 사용자의 프로필 정보를 조회합니다.")
    @GetMapping("/profile")
    public String userProfile(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (principalDetails == null) {
            redirectAttributes.addFlashAttribute("error", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        UserProfileResponseDto userProfile = new UserProfileResponseDto(principalDetails.getUser());
        model.addAttribute("user", userProfile);

        return "user/user-profile";
    }

    @Operation(summary="프로필 수정 폼", description="현재 사용자 정보를 편집할 수 있는 폼을 보여줍니다.")
    @GetMapping("/profile/edit")
    public String userProfileEdit(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            Model model,
            RedirectAttributes ra) {

        if (principalDetails == null) {
            ra.addFlashAttribute("error", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        UserProfileResponseDto userProfile  = new UserProfileResponseDto(principalDetails.getUser());
        model.addAttribute("userProfile", userProfile );

        UserUpdateRequestDto dto = UserUpdateRequestDto.builder()
                .name(userProfile .getName())
                .nickname(userProfile .getNickname())
                .birthday(userProfile .getBirthday())
                .address(userProfile .getAddress())
                .build();
        model.addAttribute("userUpdateRequestDto", dto);

        return "user/user-edit";
    }

    @Operation(summary="프로필 수정 처리", description="입력값 검증 후 프로필 정보를 업데이트합니다.")
    @ApiResponses({
            @ApiResponse(responseCode="200", description="수정 완료"),
            @ApiResponse(responseCode="400", description="검증 오류")
    })
    @PostMapping("/profile/update")
    public String updateUserProfile(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Validated @ModelAttribute("userUpdateRequestDto") UserUpdateRequestDto dto,
            BindingResult br,
            RedirectAttributes ra) {

        if (principalDetails == null) {
            ra.addFlashAttribute("error", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        if (br.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.userUpdateRequestDto", br);
            ra.addFlashAttribute("userUpdateRequestDto", dto);
            return "redirect:/user/profile/edit";
        }

        User updatedUser = userService.updateUserProfile(principalDetails.getUser().getId(), dto);

        principalDetails.setUser(updatedUser);

        ra.addFlashAttribute("success", "회원 정보가 수정되었습니다.");
        return "redirect:/user/profile";
    }

    @Operation(summary="비밀번호 변경 폼", description="로그인한 사용자 비밀번호 변경 폼을 보여줍니다.")
    @GetMapping("/change-password")
    public String changePasswordForm(Model model) {
        model.addAttribute("passwordChangeRequestDto", UserPasswordChangeRequestDto.builder().build());
        return "user/change-password";
    }

    @Operation(summary="비밀번호 변경 처리", description="현재 비밀번호 검증 후 새 비밀번호로 변경합니다.")
    @PostMapping("/change-password")
    public String changePasswordProcess(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Validated @ModelAttribute("passwordChangeRequestDto") UserPasswordChangeRequestDto dto, // 변경
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
            return "redirect:/user/change-password";
        }

        if (!passwordEncoder.matches(dto.getCurrentPassword(), principalDetails.getPassword())) {

            bindingResult.rejectValue("currentPassword", "wrongPassword", "현재 비밀번호가 올바르지 않습니다.");
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.passwordResetDto", bindingResult);
            redirectAttributes.addFlashAttribute("passwordResetDto", dto);
            return "redirect:/user/change-password";
        }

        try {
            userService.changePassword(principalDetails.getUser().getId(), dto.getNewPassword());
            redirectAttributes.addFlashAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "비밀번호 변경 중 오류가 발생했습니다.");
            return "redirect:/user/change-password";
        }

        return "redirect:/user/profile";
    }

    @Operation(summary="회원 탈퇴 폼", description="탈퇴 전 비밀번호 입력 폼을 보여줍니다.")
    @GetMapping("/resign")
    public String resignForm(Model model) {
        model.addAttribute("userResignDto", UserResignRequestDto.builder().build());
        return "user/resign";
    }

    @Operation(summary="회원 탈퇴 처리", description="비밀번호 검증 후 계정을 탈퇴 처리합니다.")
    @PostMapping("/resign")
    public String resignProcess(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Validated @ModelAttribute("userResignDto") UserResignRequestDto dto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (principalDetails == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            return "user/resign";
        }

        try {
            userService.resignUser(
                    principalDetails.getUser().getId(),
                    dto.getCurrentPassword()
            );
            return "redirect:/logout";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/user/resign";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "탈퇴 중 오류가 발생했습니다.");
            return "redirect:/user/resign";
        }
    }

    @Operation(summary="비밀번호 재설정 폼", description="링크로 받은 이메일/토큰으로 비밀번호 재설정 폼을 제공합니다.")
    @GetMapping("/reset-new-password")
    public String resetNewPasswordForm(
            @RequestParam("email") String email,
            @RequestParam("token") String token,
            Model model) {

        model.addAttribute("email", email);
        model.addAttribute("token", token);
        model.addAttribute("userPasswordResetRequestDto", UserPasswordResetRequestDto.builder().build());
        return "user/reset-password";
    }

    @Operation(summary="비밀번호 재설정 처리", description="토큰 검증 후 새 비밀번호로 설정합니다.")
    @PostMapping("/reset-new-password")
    public String handleReset(
            @RequestParam String email,
            @RequestParam String token,
            @Validated @ModelAttribute("userPasswordResetRequestDto") UserPasswordResetRequestDto dto,
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

    @Operation(summary="내가 좋아요한 글 조회", description="내가 좋아요 누른 게시글 목록을 조회합니다.")
    @GetMapping("/like-post")
    public String viewMyLikes(
            @AuthenticationPrincipal PrincipalDetails principal,
            Model model
    ) {
        Long userId = principal.getUser().getId();
        List<PostLikeDto> Postlikes = likeService.getLikedPosts(userId);
        model.addAttribute("Postlikes", Postlikes);
        return "user/user-like-post";
    }

    @Operation(summary="내가 좋아요한 술 조회", description="내가 좋아요 누른 술 목록을 조회합니다.")
    @GetMapping("/like-alcohol")
    public String viewLikedAlcohols(@AuthenticationPrincipal PrincipalDetails principal,
                                    Model model) {
        Long userId = principal.getUser().getId();
        List<AlcoholLikeDto> list = alcoholLikeService.getLikedAlcohols(userId);
        model.addAttribute("alcoholLikes", list);
        return "user/user-like-alcohol";
    }
}