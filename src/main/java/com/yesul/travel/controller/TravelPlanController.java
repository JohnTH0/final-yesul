package com.yesul.travel.controller;

import com.yesul.exception.handler.UnauthorizedException;
import com.yesul.travel.model.dto.TravelPlanRequestDto;
import com.yesul.travel.model.dto.TravelPlanDto;
import com.yesul.travel.model.entity.TravelPlan;
import com.yesul.travel.service.TravelPlanService;
import com.yesul.user.model.entity.User;
import com.yesul.user.service.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/travel-plan")
public class TravelPlanController {

    private final TravelPlanService travelPlanService;

    @GetMapping("/{id}")
    public TravelPlanDto getTravelPlanDetail(@PathVariable Long id) {
        return null;
    }

    @GetMapping("/detail")
    public ResponseEntity<TravelPlanDto> detail(int no) {
        return null;
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

        // 저장 성공만 알림
        return ResponseEntity.ok().build();
    }
}