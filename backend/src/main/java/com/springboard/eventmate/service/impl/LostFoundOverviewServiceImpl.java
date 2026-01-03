package com.springboard.eventmate.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.springboard.eventmate.model.Event;
import com.springboard.eventmate.model.LostItem;
import com.springboard.eventmate.model.User;
import com.springboard.eventmate.model.dto.OrganizerLostAndFoundSummaryDTO;
import com.springboard.eventmate.repository.EventRepository;
import com.springboard.eventmate.repository.LostItemRepository;
import com.springboard.eventmate.repository.UserRepository;
import com.springboard.eventmate.service.LostFoundOverviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LostFoundOverviewServiceImpl implements LostFoundOverviewService {

    private final EventRepository eventRepository;
    private final LostItemRepository lostItemRepository;
    private final UserRepository userRepository;

    @Override
    public Page<OrganizerLostAndFoundSummaryDTO> getOrganizerLostFoundOverview(
            int page,
            int size
    ) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User organizer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Organizer not found"));

        Pageable pageable = PageRequest.of(page, size);

        Page<Event> events =
                eventRepository.findByCreatedById(organizer.getId(), pageable);

        return events.map(event -> {

            List<LostItem> items =
                    lostItemRepository.findByEvent(event);

            int pending = 0, found = 0, returned = 0;

            for (LostItem item : items) {
                switch (item.getStatus()) {
                    case PENDING -> pending++;
                    case FOUND -> found++;
                    case RETURNED -> returned++;
                }
            }

            OrganizerLostAndFoundSummaryDTO dto =
                    new OrganizerLostAndFoundSummaryDTO();

            dto.setEventId(event.getId());
            dto.setEventTitle(event.getTitle());
            dto.setTotalLostItems(items.size());
            dto.setPendingCount(pending);
            dto.setFoundCount(found);
            dto.setReturnedCount(returned);

            return dto;
        });
    }
}
