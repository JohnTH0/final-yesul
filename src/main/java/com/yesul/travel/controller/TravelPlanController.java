package com.yesul.travel.controller;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.yesul.travel.model.dto.TravelPlanDto;
import com.yesul.travel.service.TravelPlanService;
import com.yesul.user.service.PrincipalDetails;
import com.yesul.travel.model.dto.TravelPlanRequestDto;

@Tag(name = "여행계획", description = "사용자 여행계획 목록 조회, 상세 조회, 삭제")
@Controller
@RequestMapping("/travel-plan")
@RequiredArgsConstructor
public class TravelPlanController {

    private final TravelPlanService travelPlanService;

    @Operation(summary = "여행계획 목록 페이지", description = "로그인한 사용자가 생성한 모든 여행계획을 목록으로 보여줍니다.")
    @GetMapping
    public String listPlans(
            @AuthenticationPrincipal PrincipalDetails principal,
            Model model) {
        Long userId = principal.getUser().getId();
        List<TravelPlanDto> plans = travelPlanService.listUserPlans(userId);
        model.addAttribute("travelPlans", plans);
        return "user/user-plan-list";
    }

    @Operation(summary = "여행계획 상세 페이지", description = "로그인한 사용자의 특정 여행계획 상세 정보를 보여줍니다.")
    @GetMapping("/{planId}")
    public String getPlan(
            @AuthenticationPrincipal PrincipalDetails principal,
            @PathVariable Long planId,
            Model model) {

        Long userId = principal.getUser().getId();
        TravelPlanDto plan = travelPlanService.getUserPlan(userId, planId);
        model.addAttribute("plan", plan);
        return "user/user-plan-detail";
    }

    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<?> createTravelPlan(@RequestBody TravelPlanRequestDto dto,
                                              @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails == null || principalDetails.getUser() == null || principalDetails.getUser().getId() == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Unauthorized or missing user");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        Long userId = principalDetails.getUser().getId();
        travelPlanService.saveTravelPlan(dto, userId);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "여행계획 삭제 처리", description = "로그인한 사용자의 특정 여행계획을 삭제하고 목록으로 리디렉트합니다.")
    @PostMapping("/{planId}/delete")
    public String deletePlan(
            @AuthenticationPrincipal PrincipalDetails principal,
            @PathVariable Long planId,
            RedirectAttributes ra) {

        Long userId = principal.getUser().getId();
        travelPlanService.deleteUserPlan(userId, planId);
        ra.addFlashAttribute("message", "여행계획이 삭제되었습니다.");
        return "redirect:/travel-plan";
    }
}
