package com.yesul.community.model.dto.request;

import com.yesul.community.model.entity.enums.PointType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PointRequestDto {

    @Schema(description = "유저 ID")
    private Long userId;

    @Schema(description = "포인트 활동 유형 (예: post, comment, attendance)")
    private PointType type;

    @Schema(description = "활동 내용 (예: 게시글 본문, 댓글 내용 등)")
    private String content;
}