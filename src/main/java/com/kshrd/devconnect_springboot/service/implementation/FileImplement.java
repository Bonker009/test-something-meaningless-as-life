package com.kshrd.devconnect_springboot.service.implementation;


import com.kshrd.devconnect_springboot.model.entity.FileMetadata;
import com.kshrd.devconnect_springboot.service.FileService;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileImplement implements FileService {
    @Value("${minio.bucket.name}")
    private String bucketName;

    private final MinioClient minioClient;

    @SneakyThrows
    @Override
    public FileMetadata fileUpload(MultipartFile file) {
        boolean bucketExits = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

        if (!bucketExits) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        String fileName = file.getOriginalFilename();

        fileName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(fileName);

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .contentType(file.getContentType())
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .build()
        );

        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/files/preview-file/" + fileName)
                .toUriString();

        return FileMetadata.builder()
                .fileName(fileName)
                .fileUrl(fileUrl)
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .build();
    }

    @SneakyThrows
    @Override
    public InputStream getFileByFileName(String fileName) {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build()
        );
    }

}

