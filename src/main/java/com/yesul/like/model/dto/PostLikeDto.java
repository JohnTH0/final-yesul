package com.yesul.like.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "게시글 좋아요 DTO")
public class PostLikeDto {
    private Long postId;
    private String subject;
    private String thumbnail;
    private LocalDateTime createdAt;
}