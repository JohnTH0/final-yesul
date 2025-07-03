package com.yesul.community.service;

import com.yesul.community.repository.PostImageRepository;
import com.yesul.utill.ImageUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import com.yesul.community.model.entity.Post;
import com.yesul.community.model.entity.PostImage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


@Service
@RequiredArgsConstructor
public class PostImageServiceImpl implements PostImageService {

    private final ImageUpload imageUpload;

    @Value("${app.image-upload.mode}")
    private String uploadMode;

    @Value("${app.image-upload.local-dir}")
    private String localDir;

    @Value("${app.image-upload.base-url}")
    private String baseUrl;

    @Override
    public String uploadImage(MultipartFile image) {
        if ("ncp".equalsIgnoreCase(uploadMode)) {
            return imageUpload.uploadAndGetUrl("community", image); // 도메인 커뮤니티 고정
        } else {
            throw new UnsupportedOperationException("로컬로 던져지는중?");
        }
    }

    @Override
    public void deleteImage(String imageUrl) {
        if ("ncp".equalsIgnoreCase(uploadMode)) {
            // 나중에 NCP 삭제 구현
            System.out.println("NCP에서 삭제: " + imageUrl);
        } else {
            String fileName = imageUrl.replace(baseUrl, "");
            Path deletePath = Paths.get(localDir, fileName);
            try {
                Files.deleteIfExists(deletePath);
            } catch (IOException e) {
                System.err.println("로컬 이미지 삭제 실패: " + fileName);
            }
        }
    }

    @Override
    public String extractFirstImageUrl(String contentHtml) {
        if (contentHtml == null || contentHtml.trim().isEmpty()) {
            return null;
        }

        try {
            Document doc = Jsoup.parse(contentHtml);
            Element img = doc.selectFirst("img");
            return img != null ? img.attr("src") : null;
        } catch (Exception e) {
            System.err.println("이미지 URL 추출 중 오류: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<String> getImageUrlsByPost(Post post) {
        if (post.getImages() == null || post.getImages().isEmpty()) {
            return new ArrayList<>();
        }

        return post.getImages().stream()
                .map(PostImage::getImageUrl)
                .toList();
    }
}