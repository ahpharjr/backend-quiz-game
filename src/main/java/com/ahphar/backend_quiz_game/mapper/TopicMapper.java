package com.ahphar.backend_quiz_game.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.ahphar.backend_quiz_game.DTO.TopicRequestDTO;
import com.ahphar.backend_quiz_game.DTO.TopicResponseDTO;
import com.ahphar.backend_quiz_game.exception.PhaseNotFoundException;
import com.ahphar.backend_quiz_game.models.Phase;
import com.ahphar.backend_quiz_game.models.Topic;
import com.ahphar.backend_quiz_game.repositories.PhaseRepository;

@Component
public class TopicMapper {

    private final PhaseRepository phaseRepository;

    public TopicMapper(PhaseRepository phaseRepository) {
        this.phaseRepository = phaseRepository;
    }
    
    public TopicResponseDTO toResponseDTO(Topic topic) {
        TopicResponseDTO responseDto = new TopicResponseDTO();
        responseDto.setTopicId(topic.getTopicId());
        responseDto.setName(topic.getName());
        responseDto.setImage(topic.getImage());
        responseDto.setDesc(topic.getDesc());
        responseDto.setCreatedAt(topic.getCreatedAt());
        responseDto.setPhaseName(topic.getPhase().getName());
        responseDto.setPhaseId(topic.getPhase().getPhaseId());

        return responseDto;
    }

    public Topic toModel(TopicRequestDTO requestDto) {
        Topic topic = new Topic();
        topic.setName(requestDto.getName());
        topic.setImage(requestDto.getImage());
        topic.setDesc(requestDto.getDesc());
        topic.setCreatedAt(LocalDateTime.now());

        Phase phase = phaseRepository.findById(requestDto.getPhaseId())
                .orElseThrow(() -> new PhaseNotFoundException("Phase not found with id: " + requestDto.getPhaseId()));

        topic.setPhase(phase);
        
        return topic;
    }
}
