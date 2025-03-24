package com.example.file_processor.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface UploadServiceInterface {
    void uploadFile(MultipartFile file);
}
