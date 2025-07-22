package com.yesul.notice.model.dto;

import com.yesul.notice.model.enums.NoticeType;
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
    private NoticeType type;
    private String formUrl;
    private String formId;
    private Integer point;
    private LocalDateTime createdAt;
}
