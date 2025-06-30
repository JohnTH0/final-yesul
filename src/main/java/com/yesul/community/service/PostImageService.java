package com.yesul.community.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface PostImageService {
    String uploadImage(MultipartFile image);        // 업로드 후 URL 리턴
    void deleteImage(String imageUrl);              // 이미지 삭제
    String extractFirstImageUrl(String contentHtml); // 썸네일 추출용 (optional)
}