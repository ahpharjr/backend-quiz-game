package com.ahphar.backend_quiz_game.controllers.admin;

import java.util.List;

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

@RestController
@RequestMapping("/admin/topics")
public class AdminTopicController {

    private final TopicService topicService;
    public AdminTopicController(TopicService topicService) {
        this.topicService = topicService;
    }
    
    @GetMapping
    public ResponseEntity<List<TopicResponseDTO>> getAllTopics(){
        List<TopicResponseDTO> topics = topicService.getAllTopics();
        return ResponseEntity.ok(topics);
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createTopic(@Validated @RequestBody TopicRequestDTO requestDto){
        topicService.createTopic(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Topic created successfully"));
    }

    @PutMapping("/{topicId}")
    public ResponseEntity<MessageResponse> updateTopic(@PathVariable Long topicId, 
    @Validated @RequestBody TopicRequestDTO requestDto){
        topicService.updateTopic(topicId, requestDto);
        return ResponseEntity.ok(new MessageResponse("Topic updated successfully"));
        
    }

    @DeleteMapping("/{topicId}")
    public ResponseEntity<MessageResponse> deleteTopic(@PathVariable Long topicId){
        topicService.deleteTopic(topicId);
        return ResponseEntity.ok(new MessageResponse("Topic deleted successfully"));
    }

}
