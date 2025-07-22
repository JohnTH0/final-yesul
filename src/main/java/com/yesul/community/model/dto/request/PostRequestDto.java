package com.yesul.community.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "게시글 생성 요청 DTO")
public class PostRequestDto {
    private Long id;
    private String boardName;
    private String title;
    private String content;
    private String thumbnail;
    private List<String> imageUrls;
}