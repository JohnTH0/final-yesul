package com.yesul.community.controller;

import com.yesul.community.model.dto.request.PointRequestDto;
import com.yesul.community.model.dto.response.PointHistoryResponseDto;
import com.yesul.community.model.entity.enums.PointType;
import com.yesul.community.service.PointService;
import com.yesul.user.service.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "포인트", description = "포인트 관련 API")
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

    @GetMapping("/point-history")
    @Operation(summary = "포인트 내역 페이지", description = "로그인한 사용자의 포인트 내역을 화면에 보여줍니다.")
    public String viewPointHistory(
            @AuthenticationPrincipal PrincipalDetails principal,
            Model model) {

        Long userId = principal.getUser().getId();

        List<PointHistoryResponseDto> histories = pointService
                .getPointHistories(userId)
                .stream()
                .map(PointHistoryResponseDto::from)
                .toList();

        model.addAttribute("pointHistories", histories);

        return "user/user-point-history";
    }

    @PostMapping("/use")
    @Operation(summary = "포인트 사용", description = "로그인한 사용자의 포인트를 사용합니다.")
    public String usePoint(@RequestBody PointRequestDto request) {
        pointService.usePoint(request.getUserId(), request.getType());
        return "포인트 차감 완료!";
    }

    @PostMapping("/attendance")
    @Operation(summary = "포인트 적립", description = "로그인한 사용자의 포인트를 적립합니다.")
    public ResponseEntity<?> earnAttendancePoint(@RequestParam Long userId) {
        pointService.earnPoint(userId, PointType.ATTENDANCE);
        return ResponseEntity.ok().build();
    }
}