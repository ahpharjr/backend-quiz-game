package com.ahphar.backend_quiz_game.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ahphar.backend_quiz_game.models.Topic;
import com.ahphar.backend_quiz_game.repositories.TopicRepository;

@RestController
@RequestMapping("/phases")
public class QuizSetController {
	
	@Autowired
	private TopicRepository topicRepository;
	
	@GetMapping("/{phaseId}/quizzes")
	public ResponseEntity <List<Topic>> getAllTopics(@PathVariable Long phaseId){
		List<Topic> topics = topicRepository.findByPhase_PhaseId(phaseId);
		
		return ResponseEntity.ok(topics);
	}
}
