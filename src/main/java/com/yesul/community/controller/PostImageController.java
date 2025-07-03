package com.yesul.community.controller;

import com.yesul.community.service.PostImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/post-images")
@Slf4j
public class PostImageController {

    private final PostImageService postImageService;

    // 이미지 업로드
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> upload(@RequestParam("image") MultipartFile image) {
        log.info("파일 이름: {}", image.getOriginalFilename());

        String imageUrl = postImageService.uploadImage(image);  // NCP 업로드만 수행

        Map<String, Object> result = new HashMap<>();
        result.put("url", imageUrl); // Toast UI 에디터가 이 필드명으로 탐색

        return ResponseEntity.ok(result);
    }

    // 이미지 삭제 (에디터에서 삭제시 호출)
    @DeleteMapping
    public String deleteImage(@RequestParam("url") String imageUrl) {
        postImageService.deleteImage(imageUrl); // NCP에서 삭제
        return "삭제 완료";
    }
}