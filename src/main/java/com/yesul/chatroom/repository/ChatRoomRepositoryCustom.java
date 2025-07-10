package com.yesul.chatroom.repository;

import com.yesul.chatroom.model.dto.ChatRoomSummaryResponse;
import com.yesul.chatroom.model.entity.ChatRoom;
import java.util.List;

public interface ChatRoomRepositoryCustom {
    List<ChatRoomSummaryResponse> findChatRoomsWithCursor(Long cursor, int size);
    int countTotalUnreadCount();
    List<ChatRoomSummaryResponse> searchChatRoom(String keyword);
}
