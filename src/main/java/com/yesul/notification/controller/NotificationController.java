package com.yesul.notification.controller;

import com.yesul.chatroom.model.entity.enums.Type;
import com.yesul.exception.handler.NotificationNotFoundException;
import com.yesul.notification.model.dto.response.NotificationResponseDto;
import com.yesul.notification.model.entity.Notification;
import com.yesul.notification.repository.NotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;

    //접속 시 저장된 알림 가져오기
    @GetMapping("/{receiverType}/{receiverId}")
    public List<NotificationResponseDto> getNotifications(
            @PathVariable Long receiverId,
            @PathVariable Type receiverType) {

        return notificationRepository
                .findByReceiverIdAndReceiverTypeAndIsReadFalseOrderByIdDesc(receiverId, receiverType)
                .stream()
                .map(NotificationResponseDto::fromEntity)
                .toList();
    }

    //
    @PatchMapping("/{id}/read")
    public void markAsRead(@PathVariable Long id) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("알림이 없습니다."));
        n.setRead(true);
        notificationRepository.save(n);
    }
}

