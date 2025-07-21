package com.yesul.chatroom.repository;

import com.yesul.chatroom.model.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface MessageRepositoryCustom {
    Slice<Message> getMessagesWithCursor(Long chatRoomId, Long lastMessageId, Pageable pageable);
    Slice<Message> getMessagesFirstPage(Long chatRoomId, Pageable pageable);
}
