package com.yesul.notification.model.entity;

import com.yesul.chatroom.model.entity.enums.Type;
import com.yesul.common.BaseTimeEntity;
import com.yesul.exception.handler.UserNotFoundException;
import com.yesul.notification.model.entity.enums.NotificationType;
import com.yesul.user.repository.UserRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "sender_type", nullable = false)
    private Type senderType;

    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;

    @Enumerated(EnumType.STRING)
    @Column(name = "receiver_type", nullable = false)
    private Type receiverType;

    @Column(name = "target_id", nullable = false)
    private Long targetId; // 채팅, 좋아요, 댓글 등의 ID

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    private NotificationType type;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    // Notification.java
    public String resolveSenderName(UserRepository userRepository) {
        if (senderType == Type.USER) {
            return userRepository.findById(senderId)
                    .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."))
                    .getName();
        } else
            return "관리자";
    }

}

