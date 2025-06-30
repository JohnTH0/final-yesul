package com.yesul.alcohol.controller;

import com.yesul.alcohol.model.dto.AlcoholDto;
import com.yesul.alcohol.service.AlcoholService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/alcohol")
@RequiredArgsConstructor
public class AlcoholController {

    private final AlcoholService alcoholService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(int page){
        return null;
    }

    @GetMapping("/detail")
    public ResponseEntity<AlcoholDto> detail(int no){
        return null;
    }

    @GetMapping("/regist")
    public String registForm(Model model) {
        model.addAttribute("alcoholRegisterDto");
        return "alcohol/regist";
    }

}