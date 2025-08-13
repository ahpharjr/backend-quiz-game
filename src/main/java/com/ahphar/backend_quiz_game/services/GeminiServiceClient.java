package com.ahphar.backend_quiz_game.services;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeminiServiceClient {
    
    private final String geminiUrl = "http://localhost:5000/analyze";
    private RestTemplate restTemplate = new RestTemplate();

    public String getGeminiAnalysis(String userAttemptsJson){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(userAttemptsJson, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(geminiUrl, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response .getBody();
        } else {
            throw new RuntimeException("Failed to get analysis from Gemini service: " + response.getStatusCode());
        }

    }

}
