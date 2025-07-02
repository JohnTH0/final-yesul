package com.yesul.monitoring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class MonitoringController {  // Dashboard, 공지사항

    @GetMapping("/dashboard")
    public String dashboardPage(){
        return "admin/dashboard";
    }

}