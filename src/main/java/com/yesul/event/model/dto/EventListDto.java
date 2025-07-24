package com.yesul.event.model.dto;

import com.yesul.event.model.enums.EventStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "이벤트 참여자 목록 등록요청")
public class EventListDto {
    private String formId;
    private String userName;
    private String userEmail;
    private String phone;
    private EventStatus status;
    private Long eventId;
    private LocalDateTime createdAt;
}