package com.ahphar.backend_quiz_game.services;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahphar.backend_quiz_game.DTO.TopicRequestDTO;
import com.ahphar.backend_quiz_game.DTO.TopicResponseDTO;
import com.ahphar.backend_quiz_game.exception.TopicNotFoundException;
import com.ahphar.backend_quiz_game.mapper.TopicMapper;
import com.ahphar.backend_quiz_game.models.Topic;
import com.ahphar.backend_quiz_game.repositories.TopicRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;
    private final QuizService quizService;
    
    @Cacheable(value = "topics", key = "'phaseTopics:' + #phaseId")
    public List<TopicResponseDTO> getTopicByPhaseId(Long phaseId){
        List<Topic> topics = topicRepository.findByPhase_PhaseId(phaseId);
        return topics.stream()
                .map(topicMapper::toResponseDTO)
                .toList();
    }

    public List<TopicResponseDTO> getAllTopics(){
        List<Topic> topics = topicRepository.findAll();
        return topics.stream()
                .map(topicMapper::toResponseDTO)
                .toList();
    }

    @CacheEvict(value = "topics", key = "'phaseTopics:' + #requestDTO.phaseId")
    public void createTopic(TopicRequestDTO requestDTO){
            Topic topic = topicMapper.toModel(requestDTO);

        topicRepository.save(topic);
        quizService.createQuiz(topic);
    }

    @CacheEvict(value = "topics", key = "'phaseTopics:' + #requestDTO.phaseId")
    public void updateTopic(Long topicId, TopicRequestDTO requestDTO){
        Topic existingTopic = topicRepository.findById(topicId)
                .orElseThrow(() -> new TopicNotFoundException("Topic not found with id: " + topicId));

        existingTopic.setName(requestDTO.getName());
        existingTopic.setImage(requestDTO.getImage());
        existingTopic.setDesc(requestDTO.getDesc());

        topicRepository.save(existingTopic);
    }

    @CacheEvict(value = "topics", key = "'phaseTopics:' + #phaseId")
    @Transactional
    public void deleteTopic(Long topicId, Long phaseId){
        Topic existingTopic = topicRepository.findById(topicId)
                .orElseThrow(() -> new TopicNotFoundException("Topic not found with id: " + topicId));

        topicRepository.delete(existingTopic);
    }

    public Long getPhaseIdFromTopic(Long topicId){
        Topic existingTopic = topicRepository.findById(topicId)
        .orElseThrow(() -> new TopicNotFoundException("Topic not found with id: " + topicId));

        return existingTopic.getPhase().getPhaseId();
    }
}
