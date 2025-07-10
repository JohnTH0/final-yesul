package com.yesul.chatroom.service;

import com.yesul.chatroom.model.dto.MessageRequestDto;
import com.yesul.chatroom.model.dto.MessageResponseDto;
import com.yesul.chatroom.model.entity.ChatRoom;
import com.yesul.chatroom.model.entity.Message;
import com.yesul.chatroom.model.entity.enums.Type;
import com.yesul.chatroom.repository.ChatRoomRepository;
import com.yesul.chatroom.repository.MessageRepository;
import com.yesul.exception.handler.ChatRoomNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    @Transactional
    public Slice<Message> getMessagesWithCursor(Long chatRoomId, Long lastMessageId, int size, Type readerType) {
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomNotFoundException("존재하지 않는 채팅방입니다."));

        if (readerType == Type.ADMIN) {
            chatRoom.resetAdminUnreadCount();
        } else {
            chatRoom.resetUserUnreadCount();
        }

        if (lastMessageId == null) {
            // 커서 없이 첫 페이지라면 새 JPQL 메서드 사용
            return messageRepository.getMessagesFirstPage(chatRoomId, pageable);
        } else {
            // 커서가 있으면 기존 방식 유지 (예시)
            return messageRepository.getMessagesWithCursor(chatRoomId, lastMessageId, pageable);
        }
    }


    @Override
    @Transactional
    public MessageResponseDto saveMessage(MessageRequestDto messageDto, Long senderId) {
        ChatRoom chatRoom = chatRoomRepository.findById(messageDto.getChatRoomId())
                .orElseThrow(() -> new ChatRoomNotFoundException("존재하지 않는 채팅방입니다."));

        Type senderType = messageDto.getSenderType();

        Message message = Message.builder()
                .chatRoom(chatRoom)
                .senderType(senderType)
                .messageContext(messageDto.getMessageContext())
                .messageType(messageDto.getMessageType())
                .build();

        messageRepository.save(message);

        // 수신자 정보 파악
        Type receiverType;
        Long receiverId;

        if (senderType == Type.USER) {
            receiverType = Type.ADMIN;
            receiverId = chatRoom.getAdmin().getId();
            chatRoom.increaseAdminUnreadCount(); // 관리자 안 읽은 메시지 증가
        } else {
            receiverType = Type.USER;
            receiverId = chatRoom.getUser().getId();
            chatRoom.increaseUserUnreadCount(); // 유저 안 읽은 메시지 증가
        }

        return MessageResponseDto.builder()
                .messageId(message.getId())
                .senderId(senderId)
                .receiverId(receiverId)
                .receiverType(receiverType)
                .messageContext(message.getMessageContext())
                .messageType(message.getMessageType())
                .chatRoomId(chatRoom.getId())
                .createdAt(message.getCreatedAt())
                .build();
    }

}
