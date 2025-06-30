package com.yesul.chatroom.repository;

import com.yesul.chatroom.model.entity.Message;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE m.chatRoom.id = :chatRoomId AND m.id < :lastMessageId ORDER BY m.id DESC")
    Slice<Message> getMessagesWithCursor(
            @Param("chatRoomId") Long chatRoomId,
            @Param("lastMessageId") Long lastMessageId,
            Pageable pageable
    );

}
