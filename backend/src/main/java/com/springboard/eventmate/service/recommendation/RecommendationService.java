package com.springboard.eventmate.service.recommendation;

import java.util.List;

import com.springboard.eventmate.model.dto.EventResponse;

public interface RecommendationService {
    List<EventResponse> getCuratedEvents(Long userId);
}
