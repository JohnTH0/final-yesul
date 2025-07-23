package com.yesul.chatroom.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yesul.chatroom.model.entity.enums.MessageType;
import com.yesul.chatroom.model.entity.enums.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "메시지 응답 DTO")
public class MessageResponseDto {
    private Long messageId;
    private Long senderId;
    private String senderName;
    private Type receiverType;
    private Long receiverId;
    private String messageContext;
    private MessageType messageType;
    private Long chatRoomId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
}
