package com.yesul.notification.model.dto.response;

import com.yesul.chatroom.model.entity.enums.Type;
import com.yesul.notification.model.entity.Notification;
import com.yesul.notification.model.entity.enums.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "알림 응답 DTO")
public class NotificationResponseDto {
    private Long id;
    private Long senderId;
    private Type senderType;
    private Long receiverId;
    private Type receiverType;
    private Long targetId;
    private NotificationType type;
    private String content;
    private boolean isRead;
    private LocalDateTime createdAt;

    public static NotificationResponseDto fromEntity(Notification n) {
        String content;
        if (n.getReceiverType() == Type.ADMIN) {
            content = "새로운 문의가 도착했습니다.";
        } else {
            content = "새로운 알림이 있습니다.";
        }

        return NotificationResponseDto.builder()
                .id(n.getId())
                .senderId(n.getSenderId())
                .senderType(n.getSenderType())
                .receiverId(n.getReceiverId())
                .receiverType(n.getReceiverType())
                .targetId(n.getTargetId())
                .type(n.getType())
                .isRead(n.isRead())
                .createdAt(n.getCreatedAt())
                .content(content)
                .build();
    }
}
