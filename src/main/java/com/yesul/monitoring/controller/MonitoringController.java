package com.yesul.monitoring.controller;

import com.yesul.admin.model.dto.auth.LoginAdmin;
import com.yesul.chatroom.model.entity.enums.Type;
import com.yesul.login.model.dto.AdminLoginLogDto;
import com.yesul.login.model.entity.AdminLoginLog;
import com.yesul.monitoring.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class MonitoringController {

    private final MonitoringService monitoringService;

    @GetMapping("/dashboard")
    public String dashboardPage(Model model,@AuthenticationPrincipal LoginAdmin loginAdmin) {
        int todayVisitor = monitoringService.getTodayVisitorCount();
        int realTimeUser = monitoringService.getRealTimeUserCount();

        // 시스템 모니터링
        model.addAttribute("todayVisitor", todayVisitor);
        model.addAttribute("realTimeUser", realTimeUser);
        // 알림 WebSocket용 식별자 추가
        model.addAttribute("receiverId", loginAdmin.getId());

        return "admin/dashboard";
    }

    @GetMapping("/dashboard/login-log")
    public String loginLogPage(Model model) {
        List<AdminLoginLogDto> adminLoginLog = monitoringService.getAllLoginLogs(); //형변환 / 뷰 어떻게 보여줄지
        model.addAttribute("adminLoginLog", adminLoginLog);
        return "admin/dashboard/login-log";
    }

}