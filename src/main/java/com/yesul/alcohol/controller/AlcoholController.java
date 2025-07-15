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

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/alcohols")
public class AlcoholController {

    private final AlcoholService alcoholService;
    private final ClovaService clovaService;


    // page
    @GetMapping("ai")
    public String ai() {
        return "ai/ai-chat";
    }

    @GetMapping("/unrefined-rice-wine")
    public String unrefinedRiceWine(
            AlcoholSearchDto condition,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        condition.setType("탁주");
        Page<AlcoholDetailDto> alcohols = alcoholService.searchAlcohols(condition, pageable);
        model.addAttribute("alcohols", alcohols);
        return "alcohol/unrefined-rice-wine";
    }

    @GetMapping("/herbal-rice-wine")
    public String herbalRiceWine(
            AlcoholSearchDto condition,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        condition.setType("약주");
        Page<AlcoholDetailDto> alcohols = alcoholService.searchAlcohols(condition, pageable);
        model.addAttribute("alcohols", alcohols);
        return "alcohol/herbal-rice-wine";
    }

    @GetMapping("/clear-rice-wine")
    public String clearRiceWine(
            AlcoholSearchDto condition,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        condition.setType("청주");
        Page<AlcoholDetailDto> alcohols = alcoholService.searchAlcohols(condition, pageable);
        model.addAttribute("alcohols", alcohols);
        return "alcohol/clear-rice-wine";
    }

    @GetMapping("/fruit-wine")
    public String fruitWine(
            AlcoholSearchDto condition,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        condition.setType("과실주");
        Page<AlcoholDetailDto> alcohols = alcoholService.searchAlcohols(condition, pageable);
        model.addAttribute("alcohols", alcohols);
        return "alcohol/fruit-wine";
    }

    @GetMapping("/distilled-liquor")
    public String distilledLiquor(
            AlcoholSearchDto condition,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        condition.setType("증류주");
        Page<AlcoholDetailDto> alcohols = alcoholService.searchAlcohols(condition, pageable);
        model.addAttribute("alcohols", alcohols);
        return "alcohol/distilled-liquor";
    }

    @GetMapping("/liqueur")
    public String liqueur(
            AlcoholSearchDto condition,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        condition.setType("리큐르");
        Page<AlcoholDetailDto> alcohols = alcoholService.searchAlcohols(condition, pageable);
        model.addAttribute("alcohols", alcohols);
        return "alcohol/liqueur";
    }

    @GetMapping("/detail/{id}")
    public String getAlcoholDetail(@PathVariable Long id, Model model) {
        AlcoholDetailDto alcohol = alcoholService.getAlcoholDetailById(id);

        Map<String, Integer> tasteLevels = new LinkedHashMap<>();
        tasteLevels.put("단맛", alcohol.getSweetnessLevel());
        tasteLevels.put("산미", alcohol.getAcidityLevel());
        tasteLevels.put("바디감", alcohol.getBodyLevel());
        tasteLevels.put("향", alcohol.getAromaLevel());
        tasteLevels.put("떫은맛", alcohol.getTanninLevel());
        tasteLevels.put("여운", alcohol.getFinishLevel());
        tasteLevels.put("탄산감", alcohol.getSparklingLevel());

        model.addAttribute("alcohol", alcohol);
        model.addAttribute("tasteLevels", tasteLevels);
        return "alcohol/detail";
    }

    @GetMapping("/search")
    public String search(
            AlcoholSearchDto condition,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<AlcoholDetailDto> alcohols = alcoholService.searchAlcohols(condition, pageable);
        model.addAttribute("alcohols", alcohols);
        model.addAttribute("condition", condition);
        return "alcohol/search";
    }

    // api

    @GetMapping("/{id}")
    public AlcoholDetailDto getAlcoholDetail(@PathVariable Long id) {
        return alcoholService.getAlcoholDetailById(id);
    }

    @GetMapping("/detail")
    public ResponseEntity<AlcoholDto> detail(int no){
        return null;
    }

    // 클로바
    @PostMapping("/clova")
    @ResponseBody
    public ResponseEntity<String> ask(@RequestBody ClovaAskRequestDto dto) {
        String response = clovaService.callClovaAPI(dto);
        return ResponseEntity.ok(response);
    }

    // 클로바(AI) 데이터 조회용
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

}