package com.yesul.chatroom.service;

import com.yesul.chatroom.model.dto.MessageRequestDto;
import com.yesul.chatroom.model.dto.MessageResponseDto;
import com.yesul.chatroom.model.entity.Message;
import org.springframework.data.domain.Slice;

public interface MessageService {
    Slice<Message> getMessagesWithCursor(Long chatRoomId, Long lastMessageId, int size);
    MessageResponseDto saveMessage(MessageRequestDto message, Long senderId);
}
