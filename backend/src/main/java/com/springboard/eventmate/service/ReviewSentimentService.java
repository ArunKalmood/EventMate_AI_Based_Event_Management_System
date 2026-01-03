package com.springboard.eventmate.service;

public interface ReviewSentimentService {
    void analyzeAsync(Long reviewId, String comment);
}
