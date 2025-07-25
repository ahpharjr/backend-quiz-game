package com.ahphar.backend_quiz_game.services;

import java.util.List;

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

    private TopicRepository topicRepository;
    private final TopicMapper topicMapper;

    public List<Topic> getAllTopics(long phaseId) {
        return topicRepository.findByPhase_PhaseId(phaseId);
    }
    
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

    public void createTopic(TopicRequestDTO requestDTO){
            Topic topic = topicMapper.toModel(requestDTO);

        topicRepository.save(topic);
    }

    public void updateTopic(Long topicId, TopicRequestDTO requestDTO){
        Topic existingTopic = topicRepository.findById(topicId)
                .orElseThrow(() -> new TopicNotFoundException("Topic not found with id: " + topicId));

        existingTopic.setName(requestDTO.getName());
        existingTopic.setImage(requestDTO.getImage());
        existingTopic.setDesc(requestDTO.getDesc());

        topicRepository.save(existingTopic);
    }

    @Transactional
    public void deleteTopic(Long topicId){
        Topic existingTopic = topicRepository.findById(topicId)
                .orElseThrow(() -> new TopicNotFoundException("Topic not found with id: " + topicId));
        topicRepository.delete(existingTopic);
    }
}
