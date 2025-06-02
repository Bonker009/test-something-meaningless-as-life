package com.kshrd.devconnect_springboot.service;

import com.kshrd.devconnect_springboot.model.entity.FileMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface FileService {
    FileMetadata fileUpload(MultipartFile file);

    InputStream getFileByFileName(String fileName);
}
