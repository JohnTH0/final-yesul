package com.yesul.community.model.dto;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ReviewDto {
    private Long id;
    private String alcoholName;
    private String userName;
    private String content;
    private List<String> images;
    private LocalDateTime updatedAt;
}
