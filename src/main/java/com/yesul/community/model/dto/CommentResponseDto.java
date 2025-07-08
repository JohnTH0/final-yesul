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
    private Long userId;         // 댓글 작성자의 ID
    private Boolean isAuthor;    // 현재 로그인한 사용자가 작성자인지 여부

    public static CommentResponseDto from(Comment comment, Long currentUserId) {
        boolean isAuthor = (currentUserId != null && comment.getUser().getId().equals(currentUserId));
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .nickname(comment.getUser().getNickname())
                .createdAt(comment.getCreatedAt())
                .userId(comment.getUser().getId())
                .isAuthor(isAuthor)
                .build();
    }
}