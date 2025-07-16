package com.yesul.user.controller;

import com.yesul.user.model.dto.response.UserListResponseDto;
import com.yesul.user.service.AdminUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public String userInfoPage(Model model) {

        List<UserListResponseDto> userInfoList = adminUserService.getAllUsersInfo();
        model.addAttribute("userInfoList", userInfoList);

        return "admin/user/user-management";
    }

    @PatchMapping("/{userId}/status")
    @ResponseBody
    public ResponseEntity<String> updateUserStatus(
            @PathVariable Long userId,
            @RequestParam String displayStatus) {

        adminUserService.updateUserStatus(userId, displayStatus);

        return ResponseEntity.ok("회원 상태가 변경되었습니다.");
    }
}
