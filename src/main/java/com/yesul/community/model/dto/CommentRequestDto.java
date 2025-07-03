package com.yesul.community.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CommentRequestDto {
    private Long postId;
    private String boardName;
    private String content;
}
