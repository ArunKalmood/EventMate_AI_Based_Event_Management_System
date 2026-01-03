package com.springboard.eventmate.ai.ranking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboard.eventmate.ai.groq.GroqClient;
import com.springboard.eventmate.model.Event;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroqRankingService {

    private static final String MODEL = "llama-3.1-8b-instant";

    private final GroqClient groqClient;
    
    private final ObjectMapper objectMapper = new ObjectMapper();


    public List<Event> rankEvents(List<Event> events) {

        if (events == null || events.isEmpty()) {
            return events;
        }

        String systemPrompt = """
            You are an event recommendation engine.
            Rank events based on relevance to user interests,
            popularity, urgency (date proximity), and demand.
            Return only a comma-separated list of event IDs in ranked order.
        """;

        String userPrompt = buildUserPrompt(events);

        String response =
                groqClient.chat(MODEL, systemPrompt, userPrompt);

        return reorderEvents(events, response);
    }

    // =========================
    // PROMPT BUILDER
    // =========================
    private String buildUserPrompt(List<Event> events) {

        return events.stream()
                .map(e -> String.format(
                        "ID:%d | Title:%s | Category:%s | Date:%s | Location:%s",
                        e.getId(),
                        e.getTitle(),
                        e.getCategory(),
                        e.getDate(),
                        e.getLocation()
                ))
                .collect(Collectors.joining("\n"));
    }

    // =========================
    // REORDER BASED ON AI OUTPUT
    // =========================
    private List<Event> reorderEvents(List<Event> events, String aiResponse) {

        // Absolute safety: never break recommendations
        if (events == null || events.isEmpty()) {
            return events;
        }

        if (aiResponse == null || aiResponse.isBlank()) {
            return events;
        }

        try {
            List<Long> rankedIds =
                    List.of(aiResponse.split(","))
                            .stream()
                            .map(String::trim)
                            .filter(s -> s.matches("\\d+")) // ✅ only numbers
                            .map(Long::valueOf)
                            .toList();

            // If AI output is unusable, fall back
            if (rankedIds.isEmpty()) {
                return events;
            }

            return events.stream()
                    .sorted((a, b) -> {
                        int ia = rankedIds.indexOf(a.getId());
                        int ib = rankedIds.indexOf(b.getId());

                        if (ia == -1 && ib == -1) return 0;
                        if (ia == -1) return 1;
                        if (ib == -1) return -1;

                        return Integer.compare(ia, ib);
                    })
                    .toList();

        } catch (Exception ex) {
            //  FINAL SAFETY NET — NEVER FAIL
            return events;
        }
    }

    
    public Map<Long, String> explainRecommendations(
            List<Event> events,
            List<String> userSignals
    ) {

        // =========================
        // 5.3B-2 — PROMPT TEMPLATE
        // =========================
        String promptTemplate = """
        You are an AI assistant helping explain event recommendations.

        User interests:
        %s

        Events:
        %s

        Task:
        For each event, generate ONE short, friendly explanation (max 15 words)
        explaining why this event is recommended.

        Rules:
        - Do NOT mention tracking, clicks, or monitoring
        - Keep it generic and positive
        - No emojis
        - No markdown

        Output STRICT JSON:
        {
          "eventId": "explanation"
        }
        """;

        // =========================
        // 5.3B-3 — USER SIGNAL TEXT
        // =========================
        String userSignalText = String.join(", ", userSignals);

        // =========================
        // 5.3B-3 — EVENT TEXT
        // =========================
        String eventText = events.stream()
                .map(e -> String.format(
                        "id=%d, title=%s, category=%s, location=%s",
                        e.getId(),
                        e.getTitle(),
                        e.getCategory(),
                        e.getLocation()
                ))
                .collect(Collectors.joining("\n"));

        // =========================
        // 5.3B-3 — FINAL PROMPT
        // =========================
        String finalPrompt =
                String.format(promptTemplate, userSignalText, eventText);

        // =========================
        // 5.3B-4 — CALL GROQ
        // =========================
        String groqResponse = groqClient.chat(finalPrompt);

        // TEMP LOG (debug only)
        System.out.println("Groq response: " + groqResponse);

        // =========================
        // 5.3B-5 — TEMP RETURN
        // =========================
        try {
            Map<Long, String> aiReasons =
                    objectMapper.readValue(
                            groqResponse,
                            new TypeReference<Map<Long, String>>() {}
                    );

            return aiReasons;

        } catch (Exception ex) {

            // SAFETY FALLBACK (never break UX)
            Map<Long, String> fallback = new HashMap<>();
            for (Event e : events) {
                fallback.put(
                    e.getId(),
                    "Recommended based on your recent activity"
                );
            }
            return fallback;
        }

    }

}
