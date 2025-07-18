package com.yesul.community.controller;

import com.yesul.community.model.dto.request.PointRequestDto;
import com.yesul.community.model.entity.PointHistory;
import com.yesul.community.model.entity.enums.PointType;
import com.yesul.community.service.PointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Point", description = "포인트")
@Controller
@RequestMapping("/point")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    @Operation(summary = "포인트 적립")
    @PostMapping("/earn")
    public ResponseEntity<?> earnPoint(@RequestParam Long userId,
                                       @RequestParam PointType type) {
        pointService.earnPoint(userId, type);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "포인트 내역 조회")
    @GetMapping("/history")
    public List<PointHistory> getPointHistory(@RequestParam Long userId) {
        return pointService.getPointHistories(userId);
    }

    @PostMapping("/use")
    public String usePoint(@RequestBody PointRequestDto request) {
        pointService.usePoint(request.getUserId(), request.getType());
        return "포인트 차감 완료!";
    }

    @PostMapping("/attendance")
    public ResponseEntity<?> earnAttendancePoint(@RequestParam Long userId) {
        System.out.println("== 출석 포인트 API 진입! userId=" + userId);
        pointService.earnPoint(userId, PointType.ATTENDANCE);
        return ResponseEntity.ok().build();
    }
}