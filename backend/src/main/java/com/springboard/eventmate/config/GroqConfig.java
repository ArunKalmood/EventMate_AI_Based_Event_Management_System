package com.springboard.eventmate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GroqConfig {

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.model.fast}")
    private String fastModel;

    @Value("${groq.model.smart}")
    private String smartModel;

    public String getApiKey() {
        return apiKey;
    }

    public String getFastModel() {
        return fastModel;
    }

    public String getSmartModel() {
        return smartModel;
    }
}
