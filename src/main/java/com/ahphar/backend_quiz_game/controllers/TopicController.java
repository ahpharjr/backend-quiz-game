package com.ahphar.backend_quiz_game.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ahphar.backend_quiz_game.services.TopicService;
import com.ahphar.backend_quiz_game.models.*;

@RestController
@RequestMapping("/phases")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping("/{phaseId}/topics")
    public ResponseEntity<List<Topic>> getTopics(@PathVariable long phaseId) {
        List<Topic> topics = topicService.getAllTopics(phaseId);

        return ResponseEntity.ok(topics);
    }
}
