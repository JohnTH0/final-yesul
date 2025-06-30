package com.yesul.community.model.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder

public class PostResponseDto {
    private Long id;
    private String boardName;
    private String title;
    private String content;
    private String thumbnail;
    private Integer viewCount;
    private List<String> imageUrls;
    private String nickname;
    private String createdAt;

    private Integer likeCount;
    private Boolean likedByMe;
    @Builder.Default
    private List<CommentResponseDto> comments = new ArrayList<>(); // 아직 미구현. 타입 선언만(작동위해)
}

