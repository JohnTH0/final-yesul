package com.yesul.chatroom.repository;

import com.yesul.chatroom.model.dto.response.ChatRoomSummaryResponse;
import java.util.List;

public interface ChatRoomRepositoryCustom {
    List<ChatRoomSummaryResponse> findChatRoomsWithCursor(Long cursor, int size);
    int countTotalUnreadCount();
    List<ChatRoomSummaryResponse> searchChatRoom(String keyword);
}
