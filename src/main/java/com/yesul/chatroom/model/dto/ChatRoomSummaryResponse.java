package com.yesul.chatroom.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatRoomSummaryResponse {

    private final Long roomId;
    private final String lastMessage;
    private final int adminUnreadCount;

    private final Long userId;
    private final String userName;
    private final String userProfileUrl;

}

