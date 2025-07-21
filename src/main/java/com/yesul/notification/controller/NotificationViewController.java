package com.yesul.notification.controller;

import com.yesul.admin.model.dto.auth.LoginAdmin;
import com.yesul.chatroom.model.entity.enums.Type;
import com.yesul.user.service.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "NotificationView", description = "알림 뷰 관련 API")
@Controller
@RequiredArgsConstructor
public class NotificationViewController {

    @GetMapping("/admin/notifications") // URL은 네가 원하는 대로
    @Operation(summary = "관리자 알림 페이지", description = "관리자의 알림 페이지에 필요한 정보들을 넘깁니다.")
    public String adminNotifyPage(
            Model model,
            @AuthenticationPrincipal LoginAdmin loginAdmin
    ) {
        model.addAttribute("receiverId", loginAdmin.getId());
        model.addAttribute("receiverType", Type.ADMIN);
        return "notify-sidebar";
    }

    @GetMapping("/user/notifications")
    @Operation(summary = "유저 알림 페이지", description = "유저의 알림 페이지에 필요한 정보들을 넘깁니다.")
    public String userNotifyPage(
            Model model,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        model.addAttribute("receiverId", principalDetails.getUser().getId());
        model.addAttribute("receiverType", Type.USER);
        return "header";
    }
}

