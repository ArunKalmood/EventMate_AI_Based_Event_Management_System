package com.springboard.eventmate.ai.groq;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.springboard.eventmate.config.GroqConfig;

@Component
public class GroqClient {

    private static final String GROQ_URL =
            "https://api.groq.com/openai/v1/chat/completions";

    private final RestTemplate restTemplate;
    private final GroqConfig groqConfig;

    public GroqClient(GroqConfig groqConfig) {
        this.groqConfig = groqConfig;
        this.restTemplate = new RestTemplate();
    }

    public String chat(String model, String systemPrompt, String userPrompt) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqConfig.getApiKey());

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", systemPrompt),
                        Map.of("role", "user", "content", userPrompt)
                ),
                "temperature", 0.2
        );

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(GROQ_URL, request, Map.class);

        Map<String, Object> responseBody = response.getBody();

        if (responseBody == null) {
            return "";
        }

        List<Map<String, Object>> choices =
                (List<Map<String, Object>>) responseBody.get("choices");

        if (choices == null || choices.isEmpty()) {
            return "";
        }

        Map<String, Object> message =
                (Map<String, Object>) choices.get(0).get("message");

        return message == null ? "" : (String) message.get("content");
    }
    
    public String chat(String userPrompt) {

        String model = "llama-3.1-8b-instant";
        String systemPrompt =
                "You are a helpful assistant that explains event recommendations.";

        return chat(model, systemPrompt, userPrompt);
    }

}
