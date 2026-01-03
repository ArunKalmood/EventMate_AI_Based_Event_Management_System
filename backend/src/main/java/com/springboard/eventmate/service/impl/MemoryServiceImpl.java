package com.springboard.eventmate.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.springboard.eventmate.exception.AccessDeniedException;
import com.springboard.eventmate.exception.EventNotFoundException;
import com.springboard.eventmate.model.Event;
import com.springboard.eventmate.model.Memory;
import com.springboard.eventmate.model.User;
import com.springboard.eventmate.model.dto.MemoryResponseDTO;
import com.springboard.eventmate.model.enums.BookingStatus;
import com.springboard.eventmate.model.enums.MediaType;
import com.springboard.eventmate.repository.BookingRepository;
import com.springboard.eventmate.repository.EventRepository;
import com.springboard.eventmate.repository.MemoryRepository;
import com.springboard.eventmate.repository.UserRepository;
import com.springboard.eventmate.service.CloudinaryStorageService;
import com.springboard.eventmate.service.MemoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MemoryServiceImpl implements MemoryService {

    private final EventRepository eventRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final MemoryRepository memoryRepository;
    private final CloudinaryStorageService cloudinaryStorageService;

    @Override
    public Object uploadMemory(
            Long eventId,
            MultipartFile file,
            String userEmail
    ) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }

        // 1️ Load User
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        // 2️ Load Event
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new EventNotFoundException("Event not found"));

        // 3️ Booking validation
        boolean hasBooking =
                bookingRepository.existsByUserAndEventAndStatus(
                        user,
                        event,
                        BookingStatus.CONFIRMED
                );

        if (!hasBooking) {
            throw new AccessDeniedException(
                    "You have not booked this event"
            );
        }

        // 4️ Upload to Cloudinary
        String mediaUrl = cloudinaryStorageService
                .uploadMemory(file, eventId, user.getId());

        // 5️ Detect media type
        String contentType = file.getContentType();
        MediaType mediaType =
                (contentType != null && contentType.startsWith("video"))
                        ? MediaType.VIDEO
                        : MediaType.IMAGE;

        // 6️ Persist Memory
        Memory memory = new Memory();
        memory.setUser(user);
        memory.setEvent(event);
        memory.setMediaUrl(mediaUrl);
        memory.setMediaType(mediaType);

        Memory savedMemory = memoryRepository.save(memory);

        return new MemoryResponseDTO(
                savedMemory.getId(),
                savedMemory.getMediaUrl(),
                savedMemory.getMediaType(),
                savedMemory.getEvent().getId(),
                savedMemory.getCreatedAt()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemoryResponseDTO> getMemoriesForEvent(Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new EventNotFoundException("Event not found"));

        List<Memory> memories =
                memoryRepository.findByEventIdOrderByCreatedAtDesc(event.getId());

        return memories.stream()
                .map(memory -> new MemoryResponseDTO(
                        memory.getId(),
                        memory.getMediaUrl(),
                        memory.getMediaType(),
                        memory.getEvent().getId(),
                        memory.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Object getMemoriesForUser(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        return memoryRepository
                .findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(memory -> new MemoryResponseDTO(
                        memory.getId(),
                        memory.getMediaUrl(),
                        memory.getMediaType(),
                        memory.getEvent().getId(),
                        memory.getCreatedAt()
                ))
                .toList();
    }

    @Override
    public void deleteMemory(Long memoryId, String userEmail) {

        // 1️ Load user
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        // 2️ Load memory
        Memory memory = memoryRepository.findById(memoryId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Memory not found"));

        // 3️ Ownership & role validation
        boolean isUploader =
                memory.getUser().getId().equals(user.getId());

        boolean isOrganizerOwner =
                memory.getEvent().getCreatedBy().getId()
                        .equals(user.getId());

        switch (user.getRole()) {

            case USER:
                if (!isUploader) {
                    throw new AccessDeniedException(
                            "You are not allowed to delete this memory"
                    );
                }
                break;

            case ORGANIZER:
                if (!isOrganizerOwner) {
                    throw new AccessDeniedException(
                            "You are not allowed to delete this memory"
                    );
                }
                break;

            default:
                throw new AccessDeniedException(
                        "Invalid role for memory deletion"
                );
        }

        // 4️ Fetch & validate Cloudinary asset reference
        String mediaUrl = memory.getMediaUrl();
        if (mediaUrl == null || mediaUrl.isBlank()) {
            throw new IllegalStateException(
                    "Cloudinary asset reference missing for this memory"
            );
        }

        // 5️ Delete Cloudinary asset (Task D)
        cloudinaryStorageService.deleteMemory(mediaUrl);

        // 6️ Delete Memory record from DB (Task E)
        memoryRepository.delete(memory);
    }
}
