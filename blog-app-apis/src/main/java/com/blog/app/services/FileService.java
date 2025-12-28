package com.blog.app.services;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    @Value("${project.image}")
    private String uploadDir;

    public String uploadImage(MultipartFile file, Long postId) {
        try {
            // Create folder if not exists
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            // Build file name with postId to avoid collisions
            String fileName = postId + "_" + file.getOriginalFilename();

            Path filePath = Paths.get(uploadDir + fileName);
            Files.write(filePath, file.getBytes());

            return fileName;

        } catch (IOException e) {
            throw new RuntimeException("Error uploading image: " + e.getMessage());
        }
    }
}

