package com.springboard.eventmate.model.enums;

import java.util.Arrays;

public enum EventCategory {

    MUSIC,
    BUSINESS,
    CONCERT,
    PARTY,
    FOOD,
    COMEDY,
    FESTIVAL,
    WELLNESS,
    TECH,
    WORKSHOP,
    STARTUP,
    OTHER;

    /**
     * Normalize user input / DB value to enum safely.
     * Examples:
     *  "Tech" -> TECH
     *  "technology" -> TECH
     *  "Food & Drinks" -> FOOD
     */
    public static EventCategory fromString(String value) {

        if (value == null || value.trim().isEmpty()) {
            return OTHER;
        }

        String normalized = value
                .trim()
                .toUpperCase()
                .replace("&", "")
                .replace("AND", "")
                .replace(" ", "")
                .replace("_", "");

        return Arrays.stream(values())
                .filter(cat -> cat.name().equals(normalized))
                .findFirst()
                .orElse(OTHER);
    }
}
