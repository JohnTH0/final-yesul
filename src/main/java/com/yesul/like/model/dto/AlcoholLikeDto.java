package com.yesul.like.model.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlcoholLikeDto {
    private Long alcoholId;
    private String name;
    private String thumbnailUrl;
    private LocalDateTime likedAt;
}
