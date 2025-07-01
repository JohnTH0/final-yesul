package com.yesul.chatroom.model.dto;

import com.yesul.chatroom.model.entity.enums.MessageType;
import com.yesul.chatroom.model.entity.enums.Type;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class MessageRequestDto {

    private final Long chatRoomId;
    private final Long userId;
    private final Type senderType;
    private final String messageContext;
    private final MessageType messageType;

}
