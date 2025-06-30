package com.yesul.chatroom.model.dto;

import com.yesul.chatroom.model.entity.ChatRoom;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class ChatRoomResult {

    private final ChatRoom chatRoom;
    private final boolean newlyCreated;
}
