package com.yesul.alcohol.controller;

import com.yesul.alcohol.model.dto.AlcoholDetailDto;
import com.yesul.alcohol.model.dto.AlcoholDto;
import com.yesul.alcohol.service.AlcoholService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/alcohols")
@RequiredArgsConstructor
public class AlcoholController {

    private final AlcoholService alcoholService;

    @GetMapping("/{id}")
    public AlcoholDetailDto getAlcoholDetail(@PathVariable Long id) {
        return alcoholService.getAlcoholDetailById(id);
    }

    // 예: /api/alcohols?page=0
    @GetMapping("")
    @ResponseBody
    public Page<AlcoholDetailDto> getAlcohols(
            @RequestParam(defaultValue = "0") int page
    ) {
        Pageable pageable = PageRequest.of(page, 10); // 페이지 크기 10개 고정
        return alcoholService.getAlcohols(pageable);
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