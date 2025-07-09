package com.yesul.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
public class MainController {

    @GetMapping("/")
    public String index() {
        return "index"; // resources/templates/index.html 또는 static/index.html
    }

}