package com.yesul.notification.repository;

import com.yesul.chatroom.model.entity.enums.Type;
import com.yesul.notification.model.dto.response.NotificationResponseDto;
import com.yesul.notification.model.entity.Notification;
import com.yesul.notification.model.entity.enums.NotificationType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiverIdAndReceiverTypeAndIsReadFalseOrderByIdDesc(Long receiverId, Type receiverType);
    boolean existsBySenderIdAndReceiverIdAndTargetIdAndTypeAndIsReadFalse(
            Long senderId,
            Long receiverId,
            Long targetId,
            NotificationType type
    );

}
