package com.yesul.main.controller;

import com.yesul.chatroom.model.entity.enums.Type;
import com.yesul.user.service.PrincipalDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping()
@Controller
public class MainController {

    @GetMapping("/")
    public String index(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {

        model.addAttribute("receiverId", principalDetails.getUser().getId());

        return "index";
    }


}