package com.yesul.notification.controller;

import com.yesul.admin.repository.AdminRepository;
import com.yesul.chatroom.model.entity.enums.Type;
import com.yesul.exception.handler.NotificationNotFoundException;
import com.yesul.exception.handler.UserNotFoundException;
import com.yesul.notification.model.dto.response.NotificationResponseDto;
import com.yesul.notification.model.entity.Notification;
import com.yesul.notification.repository.NotificationRepository;
import com.yesul.user.model.entity.User;
import com.yesul.user.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    @GetMapping("/{receiverType}/{receiverId}")
    public List<NotificationResponseDto> getNotifications(
            @PathVariable Long receiverId,
            @PathVariable Type receiverType) {

        List<Notification> notifications = notificationRepository
                .findByReceiverIdAndReceiverTypeAndIsReadFalseOrderByIdDesc(receiverId, receiverType);

        return notifications.stream().map(n -> {
            String senderName = getSenderName(n.getSenderId(), n.getSenderType());
            return NotificationResponseDto.fromEntity(n, senderName);
        }).toList();
    }

    private String getSenderName(Long senderId, Type senderType) {
        if (senderType == Type.USER) {
            User user = userRepository.findById(senderId)
                    .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));
            return user.getName();
        } else if (senderType == Type.ADMIN) {

            return "관리자";
        }
        return null;
    }


    @PatchMapping("/{id}/read")
    public void markAsRead(@PathVariable Long id) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("알림이 없습니다."));
        n.setRead(true);
        notificationRepository.save(n);
    }
}

