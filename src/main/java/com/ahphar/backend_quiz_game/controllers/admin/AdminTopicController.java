package com.ahphar.backend_quiz_game.controllers.admin;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ahphar.backend_quiz_game.DTO.MessageResponse;
import com.ahphar.backend_quiz_game.DTO.TopicRequestDTO;
import com.ahphar.backend_quiz_game.DTO.TopicResponseDTO;
import com.ahphar.backend_quiz_game.services.TopicService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin Topic Management", description = "APIs for managing topics in the quiz game")
@RequiredArgsConstructor
public class AdminTopicController {

    private final TopicService topicService;

    @Operation(
        summary = "Get topics by phase ID",
        description = "Retrieves all topics associated with a specific phase ID.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/phases/{phaseId}/topics")
    public ResponseEntity<?> getTopicsByPhase(@PathVariable Long phaseId){
        List<TopicResponseDTO> topics = topicService.getTopicByPhaseId(phaseId);
        if (topics.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "No topics found for phase with id: " + phaseId));
        }
        return ResponseEntity.ok(topics);
    }
    
    @Operation(
        summary = "Get all topics",
        description = "Retrieves all topics in the quiz game.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/topics")
    public ResponseEntity<List<TopicResponseDTO>> getAllTopics(){
        List<TopicResponseDTO> topics = topicService.getAllTopics();
        return ResponseEntity.ok(topics);
    }

    @Operation(
        summary = "Create a new topic",
        description = "Creates a new topic in the quiz game. Requires admin privileges.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/topic")
    public ResponseEntity<MessageResponse> createTopic(@Validated @RequestBody TopicRequestDTO requestDto){
        topicService.createTopic(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Topic created successfully"));
    }

    @Operation(
        summary = "Update an existing topic",
        description = "Updates the details of an existing topic. Requires admin privileges.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/topics/{topicId}")
    public ResponseEntity<MessageResponse> updateTopic(@PathVariable Long topicId, 
    @Validated @RequestBody TopicRequestDTO requestDto){
        topicService.updateTopic(topicId, requestDto);
        return ResponseEntity.ok(new MessageResponse("Topic updated successfully"));
        
    }

    @Operation(
        summary = "Delete a topic",
        description = "Deletes an existing topic and related flashcards from the quiz game. Requires admin privileges.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/topics/{topicId}")
    public ResponseEntity<MessageResponse> deleteTopic(@PathVariable Long topicId){
        Long phaseId = topicService.getPhaseIdFromTopic(topicId);
        topicService.deleteTopic(topicId, phaseId);
        return ResponseEntity.ok(new MessageResponse("Topic deleted successfully"));
    }

}
