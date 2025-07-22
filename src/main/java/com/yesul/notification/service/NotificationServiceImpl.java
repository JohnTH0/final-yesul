package com.yesul.notification.service;

import com.yesul.chatroom.model.entity.enums.Type;
import com.yesul.exception.handler.UserNotFoundException;
import com.yesul.notification.handler.NotificationHandler;
import com.yesul.notification.model.dto.request.CreateNotificationRequestDto;
import com.yesul.notification.model.dto.response.NotificationResponseDto;
import com.yesul.notification.model.entity.Notification;
import com.yesul.notification.model.entity.enums.NotificationType;
import com.yesul.notification.repository.NotificationRepository;
import com.yesul.user.model.entity.User;
import com.yesul.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationHandler notificationHandler;

    @Async("asyncExecutor")// 공통 로직
    public void sendNotification(CreateNotificationRequestDto dto) {

        // 0. 자기 자신에게 보내는 알림은 생성하지 않음
        if (dto.getSenderId().equals(dto.getReceiverId())) {
            return;
        }

        // 1. 같은 조건의 안 읽은 알림이 이미 있으면 새로 만들지 않는다.
        boolean exists = notificationRepository.existsBySenderIdAndReceiverIdAndTargetIdAndIsReadFalse(
                dto.getSenderId(),
                dto.getReceiverId(),
                dto.getTargetId()  // ex) chatRoomId
        );

        if (exists) {
            return;
        }

        // 2. 없으면 새로 저장
        Notification saved = notificationRepository.save(
                Notification.builder()
                        .senderId(dto.getSenderId())
                        .senderType(dto.getSenderType())
                        .receiverId(dto.getReceiverId())
                        .receiverType(dto.getReceiverType())
                        .targetId(dto.getTargetId())
                        .type(dto.getType())
                        .isRead(false)
                        .build()
        );

        // 3) WebSocket push
        NotificationResponseDto responseDto = NotificationResponseDto.builder()
                .id(saved.getId())
                .senderId(saved.getSenderId())
                .senderType(saved.getSenderType())
                .senderName(dto.getSenderName())
                .receiverId(saved.getReceiverId())
                .receiverType(saved.getReceiverType())
                .targetId(saved.getTargetId())
                .type(saved.getType())
                .boardName(dto.getBoardName())
                .content(dto.getContent())
                .isRead(saved.isRead())
                .build();

        notificationHandler.sendNotification(saved.getReceiverId(),saved.getReceiverType(), responseDto);
    }

    public void sendPostOwnerCommentNotification(Long postId, Long commenterId, Long postOwnerId, String boardName) {

        User user = userRepository.findById(commenterId)
                .orElseThrow(() -> new UserNotFoundException("해당 사용자를 찾을 수 없습니다."));

        CreateNotificationRequestDto dto = CreateNotificationRequestDto.builder()
                .senderId(commenterId)
                .senderType(Type.USER)
                .senderName(user.getName())
                .receiverId(postOwnerId)
                .receiverType(Type.USER)
                .targetId(postId)
                .type(NotificationType.COMMENT)
                .boardName(boardName)
                .content(user.getName() + "님이 댓글을 달았습니다.")
                .build();

        sendNotification(dto);
    }

    public void sendPostOwnerLikeNotification(Long postId, Long likeOwnerId, Long postOwnerId,String boardName) {

        User user = userRepository.findById(likeOwnerId)
                .orElseThrow(() -> new UserNotFoundException("해당 사용자를 찾을 수 없습니다."));

        CreateNotificationRequestDto dto = CreateNotificationRequestDto.builder()
                .senderId(likeOwnerId)
                .senderType(Type.USER)
                .senderName(user.getName())
                .receiverId(postOwnerId)
                .receiverType(Type.USER)
                .targetId(postId)
                .type(NotificationType.LIKE)
                .boardName(boardName)
                .content(user.getName() + "님이 좋아요를 눌렀습니다.")
                .build();

        sendNotification(dto);
    }
}
