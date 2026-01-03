package com.springboard.eventmate.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.springboard.eventmate.service.CloudinaryStorageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryStorageServiceImpl implements CloudinaryStorageService {

    private final Cloudinary cloudinary;

    @Override
    public String uploadMemory(MultipartFile file, Long eventId, Long userId) {

        // 1️ Validation
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be null or empty");
        }

        try {
            // 2️ Filename
        	String original = file.getOriginalFilename();
        	
        	String filename = System.currentTimeMillis() + "_" + (original != null ? original : "memory");


            // 3️ Folder structure
            String folder = "memories/event_" + eventId + "/user_" + userId;

            Map<String, Object> options = new HashMap<>();
            options.put("folder", folder);
            options.put("resource_type", "auto");

            // 4️ Upload
            Map<?, ?> uploadResult = cloudinary.uploader()
                    .upload(file.getBytes(), options);

            // 5️ Return public URL
            return uploadResult.get("secure_url").toString();

        } catch (Exception ex) {
            // 6 No silent failure
            throw new RuntimeException("Failed to upload memory to Cloudinary", ex);
        }
    }
    
    @Override
    public void deleteMemory(String mediaUrl) {

        try {
            // 1️ Extract publicId from Cloudinary URL
            // Example URL:
            // https://res.cloudinary.com/<cloud>/image/upload/v12345/folder/file.jpg

            String publicId = mediaUrl
                    .substring(mediaUrl.indexOf("/upload/") + 8)
                    .replaceAll("\\.[a-zA-Z0-9]+$", "");

            Map<String, Object> result =
                    cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            // 2️ Validate deletion result
            Object status = result.get("result");

            if (status == null || !"ok".equals(status.toString())) {
                throw new IllegalStateException(
                        "Cloudinary deletion failed for asset: " + publicId
                );
            }

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to delete Cloudinary asset", e
            );
        }
    }
    
    @Override
    public String uploadEventBanner(MultipartFile file, Long eventId) {

        // 1️ Validation
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Banner image must not be null or empty");
        }

        try {
            // 2️ Filename
            String original = file.getOriginalFilename();
            String filename =
                    "banner_" + System.currentTimeMillis() + "_" +
                    (original != null ? original : "event");

            // 3️ Folder structure
            String folder = "events/event_" + eventId + "/banner";

            Map<String, Object> options = new HashMap<>();
            options.put("folder", folder);
            options.put("resource_type", "image");

            // 4️ Upload
            Map<?, ?> uploadResult =
                    cloudinary.uploader().upload(file.getBytes(), options);

            // 5️ Return secure URL
            return uploadResult.get("secure_url").toString();

        } catch (Exception ex) {
            throw new RuntimeException("Failed to upload event banner to Cloudinary", ex);
        }
    }


}
