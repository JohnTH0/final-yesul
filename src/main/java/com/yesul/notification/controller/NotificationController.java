package com.yesul.notification.controller;

import com.yesul.chatroom.model.entity.enums.Type;
import com.yesul.exception.handler.NotificationNotFoundException;
import com.yesul.notification.model.dto.response.NotificationResponseDto;
import com.yesul.notification.model.entity.Notification;
import com.yesul.notification.repository.NotificationRepository;
import com.yesul.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Notification", description = "알림 관련 API")
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @GetMapping("/{receiverType}/{receiverId}")
    @Operation(summary = "읽지 않은 알림 조회", description = "유저가 읽지 않은 알림 리스트를 조회합니다.")
    public List<NotificationResponseDto> getNotifications(
            @PathVariable Long receiverId,
            @PathVariable Type receiverType) {

        List<Notification> notifications = notificationRepository
                .findByReceiverIdAndReceiverTypeAndIsReadFalseOrderByIdDesc(receiverId, receiverType);

        return notifications.stream()
                .map(n -> NotificationResponseDto.fromEntity(n, n.resolveSenderName(userRepository)))
                .toList();
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "알림 읽음 처리", description = "유저가 알림을 읽음 처리합니다.")
    public void markAsRead(@PathVariable Long id) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("알림이 없습니다."));
        n.setRead(true);
        notificationRepository.save(n);
    }
}

