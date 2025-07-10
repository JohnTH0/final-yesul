package com.yesul.chatroom.model.dto;

import com.yesul.chatroom.model.entity.enums.MessageType;
import com.yesul.chatroom.model.entity.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestDto {

    private Long chatRoomId;
    private Type senderType;
    private String messageContext;
    private MessageType messageType;

}
