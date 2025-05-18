package com.example.rememberconstellations.services;

import com.example.rememberconstellations.exceptions.FileSizeException;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImagesService {

    private Path fileStorageLocation;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.default-star-image}")
    private String defaultStarImage;

    @Value("${file.default-constellation-image}")
    private String defaultConstellationImage;

    @PostConstruct
    public void init() {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create upload directory", ex);
        }
    }

    public String storeFile(MultipartFile file, String entryType) {
        if (file == null || file.isEmpty()) {
            return "/static/images/" + (entryType.equals("star") ? defaultStarImage : defaultConstellationImage);
        }

        // Validate file size (5MB max)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new FileSizeException("File size exceeds 5MB limit");
        }

        String fileName = UUID.randomUUID() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
        try {
            // Validate file
            if (fileName.contains("..")) {
                throw new RuntimeException("Invalid file name: " + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName, ex);
        }
    }
}
