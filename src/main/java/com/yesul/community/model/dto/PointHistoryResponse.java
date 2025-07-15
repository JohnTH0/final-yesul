package com.yesul.community.model.dto.response;

import com.yesul.community.model.entity.PointHistory;
import com.yesul.community.model.entity.enums.PointType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 포인트 적립/차감 내역 응답 DTO
 */
@Getter
@Builder
public class PointHistoryResponse {

    @Schema(description = "활동 유형")
    private PointType type;

    @Schema(description = "적립 포인트")
    private Integer point;

    @Schema(description = "적립 여부 (true: 적립, false: 차감)")
    private Boolean isEarned;

    @Schema(description = "활동 발생 일시")
    private LocalDateTime createdAt;

    /**
     * Entity → DTO 변환 메서드
     */
    public static PointHistoryResponse from(PointHistory history) {
        return PointHistoryResponse.builder()
                .type(history.getPoint().getType())
                .point(history.getPoint().getPoint())
                .isEarned(history.getIsEarned())
                .createdAt(history.getCreatedAt())
                .build();
    }
}