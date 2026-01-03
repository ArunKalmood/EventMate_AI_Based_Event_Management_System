package com.springboard.eventmate.chat.intent;

import java.util.Map;

public class ChatIntentRouter {

    // Allowed categories (CAPS, matches DB)
    private static final Map<String, String> CATEGORY_KEYWORDS = Map.of(
            "music", "MUSIC",
            "tech", "TECH",
            "sports", "SPORTS",
            "comedy", "COMEDY",
            "art", "ART"
    );

    public static ChatIntentResult route(String message) {

        if (message == null || message.isBlank()) {
            return new ChatIntentResult(ChatIntent.FALLBACK);
        }

        String text = message.toLowerCase();

        // =========================
        // 1️ RECOMMEND
        // =========================
        if (text.contains("recommend")
                || text.contains("suggest")
                || text.contains("for me")) {

            return new ChatIntentResult(ChatIntent.RECOMMEND);
        }

        // =========================
        // 2️ TRENDING
        // =========================
        if (text.contains("trending")
                || text.contains("popular")
                || text.contains("hot")) {

            return new ChatIntentResult(ChatIntent.TRENDING);
        }

        // =========================
        // 3️ CATEGORY SEARCH
        // =========================
        for (Map.Entry<String, String> entry : CATEGORY_KEYWORDS.entrySet()) {
            if (text.contains(entry.getKey())) {

                ChatIntentResult result =
                        new ChatIntentResult(ChatIntent.CATEGORY_SEARCH);

                result.setCategory(entry.getValue()); // CAPS
                result.setKeyword(entry.getKey());

                return result;
            }
        }

        // =========================
        // 4️ FALLBACK
        // =========================
        return new ChatIntentResult(ChatIntent.FALLBACK);
    }
}
