package com.yesul.notice.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NoticeDto {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;
}
