package com.yesul.chatroom.service;

import org.springframework.web.multipart.MultipartFile;

public interface ChatImageService {
    String uploadChatImage(MultipartFile image);
}
