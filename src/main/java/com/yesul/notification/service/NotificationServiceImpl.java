package com.yesul.notification.service;

import com.yesul.notification.handler.NotificationHandler;
import com.yesul.notification.model.dto.request.CreateNotificationRequestDto;
import com.yesul.notification.model.dto.response.NotificationResponseDto;
import com.yesul.notification.model.entity.Notification;
import com.yesul.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationHandler notificationHandler;

    @Async("asyncExecutor")
    public void sendNotification(CreateNotificationRequestDto dto) {
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
                .receiverId(saved.getReceiverId())
                .receiverType(saved.getReceiverType())
                .targetId(saved.getTargetId())
                .type(saved.getType())
                .content(dto.getContent())
                .isRead(saved.isRead())
                .build();

        notificationHandler.sendNotification(saved.getReceiverId(), responseDto);
    }


}
