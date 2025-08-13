package com.ahphar.backend_quiz_game.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ahphar.backend_quiz_game.services.GeminiServiceClient;

@RestController
@RequestMapping("/gemini")
public class GeminiController {
    
        private final GeminiServiceClient geminiClient = new GeminiServiceClient();

    @PostMapping("/analyze")
    public ResponseEntity<String> analyzeUserAttempts(@RequestBody String userAttemptsJson) {

        String geminiResponse = geminiClient.getGeminiAnalysis(userAttemptsJson);
        return ResponseEntity.ok(geminiResponse);
    }
}

// These are example user attempts in JSON format that can be sent to the /analyze endpoint for analysis by the Gemini service.

// {
//   "user_attempts": [
//     {
//       "question": "What is the main goal of software project management?",
//       "user_answer": "To complete the project under budget only",
//       "correct_answer": "To deliver the project on time, within budget, and meeting quality standards",
//       "is_correct": false
//     },
//     {
//       "question": "Which of the following is a common project management methodology?",
//       "user_answer": "Waterfall",
//       "correct_answer": "Waterfall",
//       "is_correct": true
//     },
//     {
//       "question": "What is the role of a project manager?",
//       "user_answer": "To execute all project tasks personally",
//       "correct_answer": "To plan, monitor, and coordinate the project activities",
//       "is_correct": false
//     }
//   ]
// }

