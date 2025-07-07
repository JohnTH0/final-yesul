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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    @Transactional(readOnly = true)
    public Slice<Message> getMessagesWithCursor(Long chatRoomId, Long lastMessageId, int size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));
        if (lastMessageId == null) {
            return messageRepository.getMessagesFirstPage(chatRoomId, pageable);
        } else {
            return messageRepository.getMessagesWithCursor(chatRoomId, lastMessageId, pageable);
        }
    }


    @Override
    @Transactional
    public MessageResponseDto saveMessage(MessageRequestDto messageDto, Long senderId) {

        ChatRoom chatRoom = chatRoomRepository.findById(messageDto.getChatRoomId())
                .orElseThrow(() -> new ChatRoomNotFoundException("존재하지 않는 채팅방입니다."));

        //발신자 타입 확인
        Type senderType = messageDto.getSenderType();

        Message message = Message.builder()
                .chatRoom(chatRoom)
                .senderType(senderType)
                .messageContext(messageDto.getMessageContext())
                .messageType(messageDto.getMessageType())
                .build();

        messageRepository.save(message);

        Type receiverType;
        Long receiverId;
        //수신자 타입 확인
        if(senderType == Type.USER){
            receiverType = Type.ADMIN;
            receiverId = message.getChatRoom().getAdmin().getId();
        }else{
            receiverType = Type.USER;
            receiverId = message.getChatRoom().getUser().getId();
        }


        return MessageResponseDto.builder()
                .messageId(message.getId())
                .senderId(senderId)
                .receiverId(receiverId)
                .receiverType(receiverType)
                .messageContext(message.getMessageContext())
                .messageType(message.getMessageType())
                .chatRoomId(message.getChatRoom().getId())
                .createdAt(message.getCreatedAt())
                .build();
    }


}
