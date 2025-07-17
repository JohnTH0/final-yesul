package com.yesul.travel.controller;

import com.yesul.travel.model.dto.TravelPlanDto;
import com.yesul.travel.service.TravelPlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/travel-plan")
public class TravelPlanController {

    private final TravelPlanService travelPlanService;

    // api
    @GetMapping("/{id}")
    public TravelPlanDto getTravelPlanDetail(@PathVariable Long id) {
        return null;
    }

    @GetMapping("/detail")
    public ResponseEntity<TravelPlanDto> detail(int no){
        return null;
    }

}