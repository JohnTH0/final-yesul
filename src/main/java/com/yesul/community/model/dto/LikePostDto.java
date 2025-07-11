package com.yesul.community.model.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikePostDto {
    private Long postId;            // 게시글 PK
    private String subject;         // post.title
    private String thumbnail;       // post.thumbnail
    private LocalDateTime createdAt;// 좋아요 생성일
}