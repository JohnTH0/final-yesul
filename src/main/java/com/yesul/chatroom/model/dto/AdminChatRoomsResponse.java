package com.yesul.chatroom.model.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class AdminChatRoomsResponse {

    private final List<ChatRoomSummaryResponse> chatRooms;
    private final int totalUnreadCount;
    private final Long nextCursor;
}
