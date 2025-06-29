package com.backend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class StorageService {

    // Calea absolută sau relativă unde salvezi imaginile
    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public String saveProfileImage(MultipartFile file, Long userId) {
        try {
            // Creează folderul dacă nu există
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generează un nume unic pentru fișier (ex: user-5_1c3a4d.jpg)
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf('.'))
                    : "";
            String filename = "user-" + userId + "_" + UUID.randomUUID() + extension;

            Path filePath = uploadPath.resolve(filename);
            file.transferTo(filePath);

            // Returnează URL relativ la aplicație, care trebuie să fie expus public
            return "/uploads/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("Nu s-a putut salva fișierul: " + e.getMessage());
        }
    }
}
