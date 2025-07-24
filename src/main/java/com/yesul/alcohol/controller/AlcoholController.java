package com.yesul.alcohol.controller;

import com.yesul.alcohol.model.dto.AlcoholDetailDto;
import com.yesul.alcohol.model.dto.AlcoholDto;
import com.yesul.alcohol.model.dto.AlcoholSearchDto;

import com.yesul.alcohol.model.dto.ClovaAskRequestDto;
import com.yesul.alcohol.service.AlcoholService;
import com.yesul.alcohol.service.ClovaService;
import com.yesul.user.service.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Tag(name = "주류/주류 AI", description = "주류 항목별 조회 / AI 기능")
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/alcohols")
public class AlcoholController {

    private final AlcoholService alcoholService;
    private final ClovaService clovaService;


    // page
    @Operation(summary = "주류 AI 페이지", description = "주류 추천, 여행지 추천")
    @GetMapping("ai")
    public String ai() {
        return "ai/ai-chat";
    }

    @Operation(summary = "전체")
    @GetMapping("/all")
    public String all(
            AlcoholSearchDto condition,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @AuthenticationPrincipal PrincipalDetails principal,
            Model model
    ) {
        Long userId = null;
        if (principal != null && principal.getUser() != null) {
            userId = principal.getUser().getId();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<AlcoholDetailDto> alcohols = alcoholService.searchAlcoholsByUserId(condition, pageable, userId);
        model.addAttribute("alcohols", alcohols);
        return "alcohol/all";
    }

    @Operation(summary = "탁주 페이지")
    @GetMapping("/unrefined-rice-wine")
    public String unrefinedRiceWine(
            AlcoholSearchDto condition,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @AuthenticationPrincipal PrincipalDetails principal,
            Model model
    ) {
        Long userId = null;
        if (principal != null && principal.getUser() != null) {
            userId = principal.getUser().getId();
        }

        Pageable pageable = PageRequest.of(page, size);
        condition.setType("탁주");
        Page<AlcoholDetailDto> alcohols = alcoholService.searchAlcoholsByUserId(condition, pageable, userId);
        model.addAttribute("alcohols", alcohols);
        return "alcohol/unrefined-rice-wine";
    }

    @Operation(summary = "약주 페이지")
    @GetMapping("/herbal-rice-wine")
    public String herbalRiceWine(
            AlcoholSearchDto condition,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @AuthenticationPrincipal PrincipalDetails principal,
            Model model
    ) {
        Long userId = null;
        if (principal != null && principal.getUser() != null) {
            userId = principal.getUser().getId();
        }

        Pageable pageable = PageRequest.of(page, size);
        condition.setType("약주");
        Page<AlcoholDetailDto> alcohols = alcoholService.searchAlcoholsByUserId(condition, pageable, userId);
        model.addAttribute("alcohols", alcohols);
        return "alcohol/herbal-rice-wine";
    }

    @Operation(summary = "청주 페이지")
    @GetMapping("/clear-rice-wine")
    public String clearRiceWine(
            AlcoholSearchDto condition,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @AuthenticationPrincipal PrincipalDetails principal,
            Model model
    ) {
        Long userId = null;
        if (principal != null && principal.getUser() != null) {
            userId = principal.getUser().getId();
        }

        Pageable pageable = PageRequest.of(page, size);
        condition.setType("청주");
        Page<AlcoholDetailDto> alcohols = alcoholService.searchAlcoholsByUserId(condition, pageable, userId);
        model.addAttribute("alcohols", alcohols);
        return "alcohol/clear-rice-wine";
    }

    @Operation(summary = "과실주 페이지")
    @GetMapping("/fruit-wine")
    public String fruitWine(
            AlcoholSearchDto condition,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @AuthenticationPrincipal PrincipalDetails principal,
            Model model
    ) {
        Long userId = null;
        if (principal != null && principal.getUser() != null) {
            userId = principal.getUser().getId();
        }

        Pageable pageable = PageRequest.of(page, size);
        condition.setType("과실주");
        Page<AlcoholDetailDto> alcohols = alcoholService.searchAlcoholsByUserId(condition, pageable, userId);
        model.addAttribute("alcohols", alcohols);
        return "alcohol/fruit-wine";
    }

    @Operation(summary = "증류주 페이지")
    @GetMapping("/distilled-liquor")
    public String distilledLiquor(
            AlcoholSearchDto condition,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @AuthenticationPrincipal PrincipalDetails principal,
            Model model
    ) {
        Long userId = null;
        if (principal != null && principal.getUser() != null) {
            userId = principal.getUser().getId();
        }

        Pageable pageable = PageRequest.of(page, size);
        condition.setType("증류주");
        Page<AlcoholDetailDto> alcohols = alcoholService.searchAlcoholsByUserId(condition, pageable, userId);
        model.addAttribute("alcohols", alcohols);
        return "alcohol/distilled-liquor";
    }

    @Operation(summary = "리큐르 페이지")
    @GetMapping("/liqueur")
    public String liqueur(
            AlcoholSearchDto condition,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @AuthenticationPrincipal PrincipalDetails principal,
            Model model
    ) {
        Long userId = null;
        if (principal != null && principal.getUser() != null) {
            userId = principal.getUser().getId();
        }

        Pageable pageable = PageRequest.of(page, size);
        condition.setType("리큐르");
        Page<AlcoholDetailDto> alcohols = alcoholService.searchAlcoholsByUserId(condition, pageable, userId);
        model.addAttribute("alcohols", alcohols);
        return "alcohol/liqueur";
    }

    @Operation(summary = "주류 상세 페이지")
    @GetMapping("/detail/{id}")
    public String getAlcoholDetail(@PathVariable Long id,             @AuthenticationPrincipal PrincipalDetails principal,
                                   Model model
    ) {
        Long userId = null;
        if (principal != null && principal.getUser() != null) {
            userId = principal.getUser().getId();
        }

        AlcoholDetailDto alcohol = alcoholService.getAlcoholDetailWithLikeById(id, userId);

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

    @Operation(summary = "주류 검색 페이지")
    @GetMapping("/search")
    public String search(
            AlcoholSearchDto condition,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<AlcoholDetailDto> alcohols = alcoholService.searchAlcohols(condition, pageable);
        model.addAttribute("alcohols", alcohols);
        model.addAttribute("condition", condition);
        return "alcohol/search";
    }

    @Operation(summary = "주류 상세 조회 API")
    @GetMapping("/{id}")
    public AlcoholDetailDto getAlcoholDetail(@PathVariable Long id) {
        return alcoholService.getAlcoholDetailById(id);
    }

    @GetMapping("/detail")
    public ResponseEntity<AlcoholDto> detail(int no) {
        return null;
    }

    // 클로바
    @Operation(summary = "주류 추천 호출")
    @PostMapping("/clova")
    @ResponseBody
    public ResponseEntity<String> ask(@RequestBody ClovaAskRequestDto dto) {
        String response = clovaService.callClovaAPI(dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "여행지 추천 호출")
    @PostMapping("/clova2")
    @ResponseBody
    public ResponseEntity<String> ask2(@RequestBody ClovaAskRequestDto dto) {
        String response = clovaService.callClovaAPI2(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/question")
    @ResponseBody
    public String question() {
        return "관련질문이 아닙니다.";
    }

    // 클로바(AI) 데이터 조회용
    @Operation(summary = "주류 리스트 조회 API", description = "AI용 주류 조회 API")
    @GetMapping("")
    @ResponseBody
    public Page<AlcoholDetailDto> getAlcohols(
            AlcoholSearchDto condition,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return alcoholService.searchAlcohols(condition, pageable);
    }

}