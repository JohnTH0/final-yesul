package com.yesul.community.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "댓글 요청 DTO")
public class CommentRequestDto {
    private Long postId;
    private String boardName;
    private String content;
}
