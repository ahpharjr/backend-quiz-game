package com.ahphar.backend_quiz_game.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ahphar.backend_quiz_game.services.TopicService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import com.ahphar.backend_quiz_game.models.*;

@RestController
@RequestMapping("/phases")
@Tag(name = "Topics", description = "Endpoints related to topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;


    @Operation(
        summary = "Get all topics for a phase",
        description = "Returns all topics belonging to the specific phase",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{phaseId}/topics")
    public ResponseEntity<List<Topic>> getTopics(@PathVariable long phaseId) {
        List<Topic> topics = topicService.getAllTopics(phaseId);

        return ResponseEntity.ok(topics);
    }
}
