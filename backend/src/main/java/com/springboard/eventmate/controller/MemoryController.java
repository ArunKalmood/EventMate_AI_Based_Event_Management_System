package com.springboard.eventmate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboard.eventmate.service.MemoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class MemoryController {

    private final MemoryService memoryService;

    @PostMapping(
        value = "/{eventId}/memories",
        consumes = "multipart/form-data"
    )
    public ResponseEntity<?> uploadMemory(
            @PathVariable Long eventId,
            @RequestParam("file") MultipartFile file
    ) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = ((UserDetails) authentication.getPrincipal()).getUsername();

        return ResponseEntity.ok(
                memoryService.uploadMemory(eventId, file, email)
        );
    }
    
    @GetMapping("/{eventId}/memories")
    public ResponseEntity<?> getEventMemories(
            @PathVariable Long eventId
    ) {
        return ResponseEntity.ok(
                memoryService.getMemoriesForEvent(eventId)
        );
    }
    
    @GetMapping("/users/me/memories")
    public ResponseEntity<?> getMyMemories() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email =
                ((UserDetails) authentication.getPrincipal()).getUsername();

        return ResponseEntity.ok(
                memoryService.getMemoriesForUser(email)
        );
    }
    
    @DeleteMapping("/memories/{memoryId}")
    public ResponseEntity<?> deleteMemory(
            @PathVariable Long memoryId
    ) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email =
                ((UserDetails) authentication.getPrincipal()).getUsername();

        memoryService.deleteMemory(memoryId, email);

        return ResponseEntity.noContent().build();
    }



}
