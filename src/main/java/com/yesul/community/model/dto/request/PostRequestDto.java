package com.yesul.community.model.dto.request;

import lombok.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PostRequestDto {
    private Long id;
    private String boardName;
    private String title;
    private String content;
    private String thumbnail;
    private List<String> imageUrls;
}