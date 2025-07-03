package com.yesul.utill;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


@Service
@RequiredArgsConstructor
public class ImageUpload {
    private final S3Client s3Client;

    @Value("${ncp.objectstorage.endpoint}")
    private String endpoint;

    @Value("${ncp.objectstorage.bucket}")
    private String bucket;

    public String uploadAndGetUrl(String domain, MultipartFile file) {
        String key = String.format("%s/image/%s",
                domain,
                file.getOriginalFilename());
        try {
            PutObjectRequest req = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();
            s3Client.putObject(req, RequestBody.fromBytes(file.getBytes()));
            return getUrl(key);
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드 실패", e);
        }
    }

    private String getUrl(String key) {
        return String.format("%s/%s/%s", endpoint, bucket, key);
    }
}
