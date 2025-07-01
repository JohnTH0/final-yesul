package com.yesul.admin.controller;

import com.yesul.admin.model.dto.userInfo.UserInfoListAdminDto;
import com.yesul.admin.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/login")
    public void loginPage(){}

    @GetMapping("/dashboard")
    public String dashboardPage(){
        return "admin/dashboard";
    }

    @GetMapping("/session-check")
    public String sessionCheck(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 기존 세션 가져오기
        if (session != null) {
            System.out.println("세션 ID: " + session.getId());

            Object context = session.getAttribute("SPRING_SECURITY_CONTEXT");
            System.out.println("SPRING_SECURITY_CONTEXT: " + context);
        }
        return "admin/dashboard";
    }

    // --------------- 레이아웃 -----------------
    @GetMapping("/board")
    public String boardPage(){ return "admin/board/list";}

    @GetMapping("/alcohol")
    public String alcoholPage(){ return "admin/alcohol/list";}

    @GetMapping("/sale")
    public String salePage(){ return "admin/sale/list";}

    @GetMapping("/userInfo")
    public String userInfoPage(Model model){

        List<UserInfoListAdminDto> userInfoList = adminService.getAllUsersInfo();
        model.addAttribute("userInfoList", userInfoList);

        return "admin/userInfo/list";
    }

    @GetMapping("/review")
    public String reviewPage(){ return "admin/review/list";}

    @GetMapping("/chatroom")
    public String chatPage(){ return "admin/chatroom/list";}

    // --------------- 관리 기능 ----------------

    @PostMapping("/userInfo/{userId}/status")
    @ResponseBody
    public ResponseEntity<String> updateUserStatus(
            @PathVariable Long userId,
            @RequestParam String displayStatus,
            RedirectAttributes redirectAttributes) {

        adminService.updateUserStatus(userId, displayStatus);

        return ResponseEntity.ok("회원 상태가 변경되었습니다.");
    }



}