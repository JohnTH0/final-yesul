package com.yesul.chatroom.service;

import com.yesul.chatroom.model.dto.ChatRoomResult;
import com.yesul.chatroom.model.entity.ChatRoom;
import com.yesul.chatroom.repository.ChatRoomRepository;
import com.yesul.exception.handler.UserNotFoundException;
import com.yesul.user.model.entity.User;
import com.yesul.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatroomRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    // 유저 기준 채팅방 조회 또는 생성
    public ChatRoomResult findOrCreateRoom(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 유저가 존재하지 않습니다. ID=" + userId));

        Optional<ChatRoom> existing = chatroomRepository.findByUser(user);
        if (existing.isPresent()) {
            return new ChatRoomResult(existing.get(), false);
        }

        ChatRoom created = chatroomRepository.save(ChatRoom.builder()
                .user(user)
                .lastMessage("")
                .unreadCount(0)
                .build());

        return new ChatRoomResult(created, true);
    }

    }



