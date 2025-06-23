package com.yesul.user.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.yesul.user.model.dto.UserRegisterDto;
import com.yesul.user.service.UserService;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/regist")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userRegisterDto", new UserRegisterDto());
        return "user/regist";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "user/login";
    }

    @PostMapping("/regist")
    public String registerUser(@Valid @ModelAttribute("userRegisterDto") UserRegisterDto userRegisterDto,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            log.warn("회원가입 폼 유효성 검사 실패: {}", bindingResult.getAllErrors());
            return "user/regist";
        }

        if (!userRegisterDto.getLoginPwd().equals(userRegisterDto.getConfirmPwd())) {
            bindingResult.rejectValue("confirmPwd", "passwordInconsistency", "비밀번호가 일치하지 않습니다.");
            log.warn("회원가입 실패: 비밀번호 확인 불일치.");
            return "user/regist";
        }

        boolean registrationSuccess = userService.registerUser(userRegisterDto);

        if (registrationSuccess) {
            redirectAttributes.addFlashAttribute("successMessage", "회원가입을 위한 이메일이 발송되었습니다.");
            return "redirect:login";
        } else {
            if (!userService.isMailDuplicated(userRegisterDto.getMail())) {
                bindingResult.rejectValue("mail", "duplicateMail", "이미 사용 중인 이메일입니다.");
            }
            if (!bindingResult.hasErrors()) {
                bindingResult.reject("registrationFailed", "회원가입에 실패했습니다. 다시 시도해주세요.");
            }
            return "user/regist";
        }
    }
}