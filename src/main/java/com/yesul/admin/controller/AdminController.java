package com.yesul.admin.controller;

import com.yesul.admin.chart.model.dto.DashboardChartDto;
import com.yesul.admin.chart.service.ChartService;
import com.yesul.admin.model.dto.AdminLoginLogDto;
import com.yesul.admin.model.dto.auth.LoginAdmin;
import com.yesul.admin.service.AdminService;
import com.yesul.alcohol.model.dto.AlcoholDto;
import com.yesul.community.model.dto.response.PostResponseDto;
import com.yesul.community.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController {

    private final AdminService adminService;
    private final ChartService chartService;

    @GetMapping( "/dashboard")
    public String dashboardPage(Model model,@AuthenticationPrincipal LoginAdmin loginAdmin) {
        int todayVisitor = adminService.getTodayVisitorCount();
        int realTimeUser = adminService.getRealTimeUserCount();
        int totalUser = adminService.getUserCount();
        int totalAlcohol = adminService.getAlcoholCount();

        DashboardChartDto chartData = chartService.getChartData();

        List<PostResponseDto> popularPosts = adminService.getPopularPosts();
        List<AlcoholDto> popularAlcohol = adminService.popularAlcohol();

        // 시스템 모니터링
        model.addAttribute("todayVisitor", todayVisitor);
        model.addAttribute("realTimeUser", realTimeUser);

        // 전체 주류 수, 전체 회원 수
        model.addAttribute("totalUser", totalUser);
        model.addAttribute("totalAlcohol", totalAlcohol);

        // 차트
        model.addAttribute("chartData", chartData);

        // 인기순위
        model.addAttribute("popularPosts", popularPosts);
        model.addAttribute("popularAlcohol", popularAlcohol);

        // 관리자 로그인이력
        model.addAttribute("receiverId", loginAdmin.getId());

        return "admin/dashboard";
    }


    @GetMapping("/login-logs")
    @ResponseBody
    public List<AdminLoginLogDto> getLoginLogs() {
        return adminService.getAllLoginLogs();
    }
}