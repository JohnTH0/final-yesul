package com.yesul.chatroom.service;

import com.yesul.admin.model.entity.Admin;
import com.yesul.admin.repository.AdminRepository;
import com.yesul.chatroom.model.dto.AdminChatRoomsResponse;
import com.yesul.chatroom.model.dto.ChatRoomResult;
import com.yesul.chatroom.model.dto.ChatRoomSummaryResponse;
import com.yesul.chatroom.model.entity.ChatRoom;
import com.yesul.chatroom.repository.ChatRoomRepository;
import com.yesul.chatroom.repository.ChatRoomRepositoryCustom;
import com.yesul.exception.handler.AdminNotFoundException;
import com.yesul.exception.handler.UserNotFoundException;
import com.yesul.user.model.entity.User;
import com.yesul.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatroomRepository;
    private final ChatRoomRepositoryCustom chatroomRepositoryCustom;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    // 유저 기준 채팅방 조회 또는 생성
    @Override
    @Transactional
    public ChatRoomResult findOrCreateRoom(Long userId, Long adminId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 유저가 존재하지 않습니다. ID=" + userId));
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new AdminNotFoundException("해당 관리자가 존재하지 않습니다. ID=" + adminId));

        Optional<ChatRoom> existing = chatroomRepository.findByUser(user);
        if (existing.isPresent()) {
            return new ChatRoomResult(existing.get(), false);
        }

        ChatRoom created = chatroomRepository.save(ChatRoom.builder()
                .user(user)
                .lastMessage("")
                .userUnreadCount(0)
                .admin(admin)
                .build());

        return new ChatRoomResult(created, true);
    }
    //admin 기준 채팅방 리스트 조회
    @Override
    public AdminChatRoomsResponse getAdminChatRooms(Long cursor, int size) {

        List<ChatRoomSummaryResponse> summaries = chatroomRepositoryCustom.findChatRoomsWithCursor(cursor, size);

        int totalUnread = chatroomRepositoryCustom.countTotalUnreadCount();

        Long nextCursor = summaries.isEmpty() ? null :
                summaries.get(summaries.size() - 1).getRoomId();

        return AdminChatRoomsResponse.builder()
                .chatRooms(summaries)
                .totalUnreadCount(totalUnread)
                .nextCursor(nextCursor)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatRoomSummaryResponse> searchChatRoom(String keyword) {
        return chatroomRepositoryCustom.searchChatRoom(keyword);
    }
}



