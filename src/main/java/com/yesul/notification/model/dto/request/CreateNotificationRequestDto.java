package com.yesul.notification.model.dto.request;

import com.yesul.chatroom.model.entity.enums.Type;
import com.yesul.notification.model.entity.enums.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "알림 생성 요청 DTO")
public class CreateNotificationRequestDto {

    private Long senderId;
    private Type senderType;
    private Long receiverId;
    private Type receiverType;
    private Long targetId;
    private NotificationType type;
    private String content;

}
