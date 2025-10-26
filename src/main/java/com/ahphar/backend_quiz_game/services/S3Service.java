package com.ahphar.backend_quiz_game.services;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName = "quizgame-assets-bucket";
    private final String folderPath = "quizgame-assets/images/phases/";

    public String uploadFile(MultipartFile file) {
        try {
            String fileExtension = getFileExtension(file.getOriginalFilename());
            String fileName = folderPath + UUID.randomUUID() + "_" + Instant.now().toEpochMilli() + fileExtension;

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .acl("public-read")
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return "https://" + bucketName + ".s3.us-east-1.amazonaws.com/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf("."));
    }
}