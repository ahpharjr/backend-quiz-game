package com.ahphar.backend_quiz_game.controllers;

import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ahphar.backend_quiz_game.DTO.QuizLeaderboardResponseDTO;

@RestController
@RequestMapping("/ws-test")
public class WebSocketTestController {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketTestController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/send/{quizId}")
    public void sendFakeLeaderboard(@PathVariable Long quizId) {
        messagingTemplate.convertAndSend("/topic/leaderboard/" + quizId,
                List.of(new QuizLeaderboardResponseDTO("FakeUser", 100, 60000L, "/img1.png")));
    }
}

