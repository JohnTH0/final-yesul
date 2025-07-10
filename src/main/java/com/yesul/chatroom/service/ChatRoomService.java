package com.yesul.chatroom.service;

import com.yesul.chatroom.model.dto.AdminChatRoomsResponse;
import com.yesul.chatroom.model.dto.ChatRoomResult;
import com.yesul.chatroom.model.dto.ChatRoomSummaryResponse;
import java.util.List;

public interface ChatRoomService {
    ChatRoomResult findOrCreateRoom(Long userId, Long admin_id);
    AdminChatRoomsResponse getAdminChatRooms(Long cursor, int size);
    List<ChatRoomSummaryResponse> searchChatRoom(String keyword);
}
