package com.yesul.chatroom.controller.common;

import com.yesul.chatroom.service.ChatImageService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/chat-images")
@RequiredArgsConstructor
@Slf4j
public class ChatImageController {

    private final ChatImageService chatImageService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> upload(@RequestParam("image") MultipartFile image) {

        String imageUrl = chatImageService.uploadChatImage(image);

        Map<String, Object> result = new HashMap<>();
        result.put("imageUrl", imageUrl);

        return ResponseEntity.ok(result);
    }
}
