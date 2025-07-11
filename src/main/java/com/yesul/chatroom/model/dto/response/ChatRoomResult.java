package com.yesul.chatroom.model.dto.response;

import com.yesul.chatroom.model.entity.ChatRoom;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatRoomResult {

    private final ChatRoom chatRoom;
    private final boolean newlyCreated;
}
