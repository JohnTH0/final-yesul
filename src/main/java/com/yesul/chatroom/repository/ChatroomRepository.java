package com.yesul.chatroom.repository;

import com.yesul.chatroom.model.entity.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomRepository extends JpaRepository<Chatroom, Integer> {
}