package com.yesul.community.model.dto;

import com.yesul.community.model.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommentResponseDto {

    private Long id;
    private String content;
    private String nickname;
    private LocalDateTime createdAt;

    public static CommentResponseDto from(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .nickname(comment.getUser().getNickname())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}