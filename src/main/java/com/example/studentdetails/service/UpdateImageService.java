package com.example.studentdetails.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UpdateImageService {
    String uploadImage(String path, MultipartFile file) throws IOException;

}
