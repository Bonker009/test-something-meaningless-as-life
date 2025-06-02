package com.kshrd.devconnect_springboot.controller;

import com.kshrd.devconnect_springboot.base.ApiResponse;
import com.kshrd.devconnect_springboot.base.BaseController;
import com.kshrd.devconnect_springboot.model.entity.FileMetadata;
import com.kshrd.devconnect_springboot.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("api/v1/files")
@RequiredArgsConstructor
public class FileController extends BaseController {

    private final FileService fileService;

    @PostMapping(value = "/upload-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FileMetadata>> uploadFile(@RequestParam MultipartFile file){
        return response("Upload file successfully!",HttpStatus.CREATED,fileService.fileUpload(file));
    }

    @GetMapping("/preview-file/{file-name}")
    public ResponseEntity<?> getFileByFileName(@PathVariable("file-name") String fileName) throws IOException {
        InputStream inputStream = fileService.getFileByFileName(fileName);
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if (!fileName.endsWith(".pdf")) {
            mediaType = MediaType.IMAGE_PNG;
        }
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(mediaType)
                .body(inputStream.readAllBytes());
    }

}
