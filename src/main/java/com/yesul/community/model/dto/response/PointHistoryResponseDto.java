package com.yesul.community.model.dto.response;

import com.yesul.community.model.entity.PointHistory;
import com.yesul.community.model.entity.enums.PointType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "포인트 history 응답 DTO")
public class PointHistoryResponseDto {

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
    public static PointHistoryResponseDto from(PointHistory history) {
        return PointHistoryResponseDto.builder()
                .type(history.getPoint().getType())
                .point(history.getPoint().getPoint())
                .isEarned(history.getIsEarned())
                .createdAt(history.getCreatedAt())
                .build();
    }
}