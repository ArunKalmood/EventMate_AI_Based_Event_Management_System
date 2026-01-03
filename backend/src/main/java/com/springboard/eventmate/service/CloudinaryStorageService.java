package com.springboard.eventmate.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryStorageService {

    String uploadMemory(MultipartFile file, Long eventId, Long userId);
    
    void deleteMemory(String mediaUrl); //EV 359
    
    String uploadEventBanner(MultipartFile file, Long eventId);

}
