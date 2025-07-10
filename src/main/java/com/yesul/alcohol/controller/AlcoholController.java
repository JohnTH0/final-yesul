package com.yesul.alcohol.controller;

import com.yesul.alcohol.model.dto.AlcoholDetailDto;
import com.yesul.alcohol.model.dto.AlcoholDto;
import com.yesul.alcohol.model.dto.AlcoholSearchDto;

import com.yesul.alcohol.model.dto.ClovaAskRequestDto;
import com.yesul.alcohol.service.AlcoholService;
import com.yesul.alcohol.service.ClovaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/alcohols")
public class AlcoholController {

    private final AlcoholService alcoholService;
    private final ClovaService clovaService;

    @GetMapping("ai")
    public String ai() {
        return "ai/ai-chat";
    }

    @PostMapping("/clova")
    @ResponseBody
    public ResponseEntity<String> ask(@RequestBody ClovaAskRequestDto dto) {
        String response = clovaService.callClovaAPI(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public AlcoholDetailDto getAlcoholDetail(@PathVariable Long id) {
        return alcoholService.getAlcoholDetailById(id);
    }

    // 예: /api/alcohols?page=0
    @GetMapping("")
    @ResponseBody
    public Page<AlcoholDetailDto> getAlcohols(
            AlcoholSearchDto condition,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return alcoholService.searchAlcohols(condition, pageable);
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