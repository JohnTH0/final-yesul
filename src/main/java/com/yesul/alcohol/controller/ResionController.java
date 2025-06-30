package com.yesul.alcohol.controller;

import com.yesul.alcohol.model.dto.AlcoholDto;
import com.yesul.alcohol.service.AlcoholService;
import com.yesul.alcohol.service.RegionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/alcohol")
@RequiredArgsConstructor
public class ResionController {

    private final RegionService regionService;

    // GET /regions/{province}/alcohols?page=0&size=10
    @GetMapping("/{province}/alcohols")
    public Page<AlcoholDto> getAlcoholsByRegion(
            @PathVariable String province,
            Pageable pageable) {
        return regionService.getAlcoholsByRegion(province, pageable);
    }

}