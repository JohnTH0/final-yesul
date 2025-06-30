package com.yesul.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


@Service
@RequiredArgsConstructor
public class PostImageServiceImpl implements PostImageService {

    @Value("${app.image-upload.mode}")
    private String uploadMode;

    @Value("${app.image-upload.local-dir}")
    private String localDir;

    @Value("${app.image-upload.base-url}")
    private String baseUrl;

    @Override
    public String uploadImage(MultipartFile image) {
        if ("ncp".equalsIgnoreCase(uploadMode)) {
            // 나중에 NCP 구현
            throw new UnsupportedOperationException("NCP 업로드는 아직 구현되지 않았습니다.");
        } else {
            return uploadLocal(image);
        }
    }

    private String uploadLocal(MultipartFile image) {
        try {
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path savePath = Paths.get(localDir, fileName);
            Files.createDirectories(savePath.getParent());
            Files.write(savePath, image.getBytes());
            return baseUrl + fileName;
        } catch (IOException e) {
            throw new RuntimeException("로컬 이미지 업로드 실패", e);
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
        Document doc = Jsoup.parse(contentHtml);
        Element img = doc.selectFirst("img");
        return img != null ? img.attr("src") : null;
    }
}