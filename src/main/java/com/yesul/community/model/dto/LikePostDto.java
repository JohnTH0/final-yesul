package com.yesul.community.model.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikePostDto {
    private Long postId;
    private String subject;
    private String thumbnail;
    private LocalDateTime createdAt;
}