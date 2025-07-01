package com.yesul.chatroom.service;

import com.yesul.chatroom.model.dto.ChatRoomResult;

public interface ChatRoomService {
    ChatRoomResult findOrCreateRoom(Long userId, Long admin_id);
}
