package com.yesul.notification.service;

import com.yesul.notification.model.dto.request.CreateNotificationRequestDto;

public interface NotificationService {
    void sendNotification(CreateNotificationRequestDto dto);
}
