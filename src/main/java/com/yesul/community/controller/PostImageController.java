package com.yesul.community.controller;

import com.yesul.community.service.PostImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post-images")
@Slf4j
public class PostImageController {

    private final PostImageService postImageService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> upload(@RequestParam("image") MultipartFile image) {
        log.info("파일 이름: {}", image.getOriginalFilename());

        String imageUrl = postImageService.uploadImage(image);  // 실제 서비스 호출

        Map<String, Object> result = new HashMap<>();
        result.put("url", imageUrl); // Toast UI 에디터는 이 필드 필요

        return ResponseEntity.ok(result);
    }

    @DeleteMapping
    public String deleteImage(@RequestParam("url") String imageUrl) {
        postImageService.deleteImage(imageUrl);
        return "삭제 완료";
    }
}