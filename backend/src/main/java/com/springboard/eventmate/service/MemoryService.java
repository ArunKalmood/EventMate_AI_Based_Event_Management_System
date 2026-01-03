package com.springboard.eventmate.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.springboard.eventmate.model.dto.MemoryResponseDTO;

public interface MemoryService {

    Object uploadMemory(
        Long eventId,
        MultipartFile file,
        String userEmail
    );
    
    List<MemoryResponseDTO> getMemoriesForEvent(Long eventId);
    
    Object getMemoriesForUser(String userEmail);
    
    void deleteMemory(Long memoryId, String userEmail);


}
