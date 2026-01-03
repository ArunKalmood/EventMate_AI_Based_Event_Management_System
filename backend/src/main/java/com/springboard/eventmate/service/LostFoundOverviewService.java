package com.springboard.eventmate.service;

import org.springframework.data.domain.Page;

import com.springboard.eventmate.model.dto.OrganizerLostAndFoundSummaryDTO;

public interface LostFoundOverviewService {

    Page<OrganizerLostAndFoundSummaryDTO> getOrganizerLostFoundOverview(
            int page,
            int size
    );
}
