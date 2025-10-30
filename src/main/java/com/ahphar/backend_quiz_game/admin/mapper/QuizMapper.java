package com.ahphar.backend_quiz_game.admin.mapper;

import org.springframework.stereotype.Component;

import com.ahphar.backend_quiz_game.admin.DTOs.QuizRespDTO;
import com.ahphar.backend_quiz_game.models.Quiz;

@Component
public class QuizMapper {
    
    public QuizRespDTO toDTO(Quiz quiz){
        QuizRespDTO dto = new QuizRespDTO();

        dto.setQuizId(quiz.getQuizId());
        dto.setName(quiz.getTopic().getName());
        dto.setImage(quiz.getTopic().getImage());
        dto.setDesc(quiz.getTopic().getDesc());
        dto.setTopicId(quiz.getTopic().getTopicId());
        dto.setCreatedAt(quiz.getTopic().getCreatedAt());

        return dto;
    }
}
