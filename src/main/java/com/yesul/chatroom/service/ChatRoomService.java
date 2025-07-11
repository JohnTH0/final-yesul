package com.yesul.chatroom.service;

import com.yesul.chatroom.model.dto.response.AdminChatRoomsResponse;
import com.yesul.chatroom.model.dto.response.ChatRoomResult;
import com.yesul.chatroom.model.dto.response.ChatRoomSummaryResponse;
import java.util.List;

public interface ChatRoomService {
    ChatRoomResult findOrCreateRoom(Long userId, Long admin_id);
    AdminChatRoomsResponse getAdminChatRooms(Long cursor, int size);
    List<ChatRoomSummaryResponse> searchChatRoom(String keyword);
}
