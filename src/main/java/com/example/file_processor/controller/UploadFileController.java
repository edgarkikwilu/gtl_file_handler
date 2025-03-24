package com.example.file_processor.controller;

import com.example.file_processor.interfaces.UploadServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("upload/v1")
//@RequiredArgsConstructor
public class UploadFileController {
    @Autowired
    private UploadServiceInterface uploadServiceInterface;

    UploadFileController(UploadServiceInterface uploadServiceInterface){
        this.uploadServiceInterface = uploadServiceInterface;
    }

    @RequestMapping(consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file){
        uploadServiceInterface.uploadFile(file);
        return ResponseEntity.ok("ok");
    }
}
