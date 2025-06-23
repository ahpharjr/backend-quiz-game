package com.ahphar.backend_quiz_game.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ahphar.backend_quiz_game.models.Topic;
import com.ahphar.backend_quiz_game.repositories.TopicRepository;

@Service
public class TopicService {

    private TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public List<Topic> getAllTopics(long phaseId) {
        return topicRepository.findByPhase_PhaseId(phaseId);
    }
}
