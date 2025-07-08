package com.yesul.chatroom.service;

import com.yesul.utill.ImageUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ChatImageServiceImpl implements ChatImageService {

    private final ImageUpload imageUpload;

    @Value("${app.image-upload.mode}")
    private String uploadMode;

    @Override
    public String uploadChatImage(MultipartFile image) {
        if ("ncp".equalsIgnoreCase(uploadMode)) {
            return imageUpload.uploadAndGetUrl("chat", image);
        } else {
            throw new UnsupportedOperationException("지원하지 않는 동작입니다.");
        }
    }
}
