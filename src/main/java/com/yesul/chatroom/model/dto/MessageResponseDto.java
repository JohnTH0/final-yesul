package com.yesul.chatroom.model.dto;

import com.yesul.chatroom.model.entity.enums.MessageType;
import com.yesul.chatroom.model.entity.enums.Type;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageResponseDto {
    private final Long messageId;
    private final Long senderId;
    private final Type receiverType;
    private final Long receiverId;
    private final String messageContext;
    private final MessageType messageType;
    private final Long chatRoomId;
    private final LocalDateTime createdAt;
}
