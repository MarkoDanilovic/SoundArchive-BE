package com.example.soundarchive.service;

import com.example.soundarchive.exception.DataNotFoundException;
import com.example.soundarchive.exception.InternalServerErrorException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class ImageUploadService {

    private static final long MAX_FILE_SIZE = 20 * 1024 * 1024; // 20MB
    private static final List<String> ALLOWED_EXTENSIONS = List.of("png", "jpg", "jpeg");

    private static final String BASE_UPLOAD_PATH = "/app/uploads";

    public String uploadImage(MultipartFile file, String folder, String filenameBase, String existingImagePath) {

        try {
            if (existingImagePath != null && !existingImagePath.isEmpty()) {
                deleteImage(existingImagePath);
            }

            if (file.getSize() > MAX_FILE_SIZE) {
                throw new IllegalArgumentException("File size exceeds 20MB.");
            }

            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            if (!isAllowedExtension(extension)) {
                throw new IllegalArgumentException("Only PNG, JPG, and JPEG files are allowed.");
            }

            File uploadDir = new File(BASE_UPLOAD_PATH + "/" + folder);
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                if (!created) {
                    throw new InternalServerErrorException("Failed to create upload directory.");
                }
            }

            String storedFileName = filenameBase + "." + extension.toLowerCase();
            File destination = new File(uploadDir, storedFileName);
            file.transferTo(destination);

            return "/" + folder + "/" + storedFileName;
        } catch (IOException e) {
            throw new InternalServerErrorException("Failed to store the image file. Error: " + e.getMessage());
        }
    }

    public void deleteImage(String imagePath) {
        File file = new File(BASE_UPLOAD_PATH + imagePath);
        System.out.println("\n\n\nImage path: " + BASE_UPLOAD_PATH + imagePath + "\n\n\n");
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                throw new InternalServerErrorException("Failed to delete image at path: " + imagePath);
            }
        } else {
            throw new DataNotFoundException("Image not found at path: " + imagePath);
        }
    }

    private String getFileExtension(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf('.') + 1);
        }
        return "";
    }

    private boolean isAllowedExtension(String extension) {
        for (String allowed : ALLOWED_EXTENSIONS) {
            if (allowed.equalsIgnoreCase(extension)) return true;
        }
        return false;
    }
}
