package com.yesul.community.service;

import org.springframework.web.multipart.MultipartFile;
import com.yesul.community.model.entity.Post;
import java.util.List;

public interface PostImageService {
    String uploadImage(MultipartFile image);        // 업로드 후 URL 리턴
    void deleteImage(String imageUrl);              // 이미지 삭제
    String extractFirstImageUrl(String contentHtml); // 썸네일 추출용 (optional)
    List<String> getImageUrlsByPost(Post post);    // 게시글의 이미지 URL 리스트 조회
    List<String> extractImageUrlsFromContent(String content);
}