package com.springboard.eventmate.chat.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboard.eventmate.chat.dto.ChatEventDTO;
import com.springboard.eventmate.chat.dto.ChatQueryRequest;
import com.springboard.eventmate.chat.dto.ChatQueryResponse;
import com.springboard.eventmate.chat.intent.ChatIntentResult;
import com.springboard.eventmate.chat.intent.ChatIntentRouter;
import com.springboard.eventmate.model.dto.EventResponse;
import com.springboard.eventmate.model.dto.EventSummaryResponseDTO;
import com.springboard.eventmate.service.EventService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final EventService eventService;

    @PostMapping("/query")
    public ResponseEntity<ChatQueryResponse> query(
            @Valid @RequestBody ChatQueryRequest request,
            Authentication auth
    ) {

        if (auth == null) {
            return ResponseEntity.ok(
                    new ChatQueryResponse(
                            "Please log in to discover events.",
                            List.of()
                    )
            );
        }

        String userEmail = auth.getName();
        String message = request.getMessage();

        ChatIntentResult intentResult =
                ChatIntentRouter.route(message);

        switch (intentResult.getIntent()) {

            // =========================
            // RECOMMEND / TRENDING
            // =========================
            case RECOMMEND, TRENDING -> {
                List<EventResponse> events =
                        eventService.getCuratedEvents(userEmail);

                return ResponseEntity.ok(
                        new ChatQueryResponse(
                                "Here are some events you may like.",
                                toChatEvents(events)
                        )
                );
            }

            // =========================
            // CATEGORY SEARCH
            // =========================
            case CATEGORY_SEARCH -> {

                List<EventSummaryResponseDTO> events =
                        eventService.searchEvents(
                        		"",
                                intentResult.getCategory(), // CAPS
                                0,
                                5,	
                                userEmail
                        ).getContent();

                return ResponseEntity.ok(
                        new ChatQueryResponse(
                                "Here are some " + intentResult.getCategory().toLowerCase() + " events.",
                                events.stream()
                                        .map(e -> new ChatEventDTO(
                                                e.getEventId(),
                                                e.getTitle(),
                                                "Live " + intentResult.getCategory().toLowerCase() + " event" 
                                        ))
                                        .toList()
                        )
                );
            }

            // =========================
            // FALLBACK
            // =========================
            default -> {
                return ResponseEntity.ok(
                        new ChatQueryResponse(
                                "I can help you discover events. Try asking about music, tech, or recommendations.",
                                List.of()
                        )
                );
            }
        }
    }

    // =========================
    // Mapper (lightweight)
    // =========================
    private List<ChatEventDTO> toChatEvents(List<EventResponse> events) {
        return events.stream()
                .limit(5)
                .map(e -> new ChatEventDTO(
                        e.getId(),
                        e.getTitle(),
                        e.getRecommendationReason()
                ))
                .toList();
    }
}
