package com.ahphar.backend_quiz_game.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ahphar.backend_quiz_game.DTO.TopicResponseDTO;
import com.ahphar.backend_quiz_game.services.TopicService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/phases")
@Tag(name = "Quiz Sets", description = "Endpoints related to quiz sets (topics) under a specific phase")
@RequiredArgsConstructor
public class QuizSetController {
	
	private final TopicService topicService;

	@Operation(
        summary = "Get all quiz sets (topics) for a phase",
        description = "Returns all quiz sets (topics) belonging to the specified phase ID.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
	@GetMapping("/{phaseId}/quizzes")
    public ResponseEntity<List<TopicResponseDTO>> getTopics(@PathVariable long phaseId) {
        List<TopicResponseDTO> topics = topicService.getTopicByPhaseId(phaseId);

        return ResponseEntity.ok(topics);
    }
}
