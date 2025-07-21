package com.yesul.chatroom.model.dto.request;

import com.yesul.chatroom.model.entity.enums.MessageType;
import com.yesul.chatroom.model.entity.enums.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "메시지 생성 요청 DTO")
public class MessageRequestDto {

    private Long chatRoomId;
    private Type senderType;
    private String messageContext;
    private MessageType messageType;

}
