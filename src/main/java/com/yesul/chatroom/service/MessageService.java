package com.yesul.chatroom.service;

import com.yesul.chatroom.model.dto.MessageRequestDto;
import com.yesul.chatroom.model.dto.MessageResponseDto;
import com.yesul.chatroom.model.entity.Message;
import com.yesul.chatroom.model.entity.enums.Type;
import org.springframework.data.domain.Slice;

public interface MessageService {
    Slice<Message> getMessagesWithCursor(Long chatRoomId, Long lastMessageId, int size, Type type);
    MessageResponseDto saveMessage(MessageRequestDto message, Long senderId);
}
