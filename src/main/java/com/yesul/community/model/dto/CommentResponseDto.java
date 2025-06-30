package com.yesul.community.model.dto;

import lombok.*;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @Builder
    public class CommentResponseDto {
        private String nickname;
        private String content;
        private String createdAt;
    }