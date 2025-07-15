package com.yesul.notification.controller;

import com.yesul.admin.model.dto.auth.LoginAdmin;
import com.yesul.chatroom.model.entity.enums.Type;
import com.yesul.user.service.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class NotificationViewController {

    @GetMapping("/admin/notifications") // URL은 네가 원하는 대로
    public String adminNotifyPage(
            Model model,
            @AuthenticationPrincipal LoginAdmin loginAdmin
    ) {
        model.addAttribute("receiverId", loginAdmin.getId());
        model.addAttribute("receiverType", Type.ADMIN);
        return "notify-sidebar";
    }

    @GetMapping("/user/notifications")
    public String userNotifyPage(
            Model model,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        model.addAttribute("receiverId", principalDetails.getUser().getId());
        model.addAttribute("receiverType", Type.USER);
        return "header";
    }
}

