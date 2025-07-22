package com.yesul.chatroom.controller.common;

import com.yesul.chatroom.service.ChatImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "채팅 이미지", description = "채팅 이미지 관련 API")
@RestController
@RequestMapping("/chat-images")
@RequiredArgsConstructor
public class ChatImageController {

    private final ChatImageService chatImageService;

    @PostMapping("/upload")
    @Operation(summary = "채팅 이미지 업로드 API", description = "채팅방에 이미지를 업로드합니다.")
    public ResponseEntity<Map<String, Object>> upload(@RequestParam("image") MultipartFile image) {

        String imageUrl = chatImageService.uploadChatImage(image);

        Map<String, Object> result = new HashMap<>();
        result.put("imageUrl", imageUrl);

        return ResponseEntity.ok(result);
    }
}
