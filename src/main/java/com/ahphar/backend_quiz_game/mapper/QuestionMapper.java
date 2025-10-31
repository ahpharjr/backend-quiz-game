package com.ahphar.backend_quiz_game.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.ahphar.backend_quiz_game.DTO.QuestionRequestDTO;
import com.ahphar.backend_quiz_game.DTO.QuestionResponseDTO;
import com.ahphar.backend_quiz_game.models.Question;

@Component
public class QuestionMapper {

    public QuestionResponseDTO toDto(Question question, Long phaseId){
        QuestionResponseDTO dto = new QuestionResponseDTO();
        dto.setQuestionId(question.getQuestionId());
        dto.setQuestionText(question.getQuestion());
        dto.setImage(question.getImage());
        dto.setCreatedAt(question.getCreatedAt());
        dto.setQuizName(question.getQuiz().getTopic().getName());
        dto.setPhaseId(phaseId);
        return dto;
    }

    public Question toModel(QuestionRequestDTO requestDTO) {
        Question question = new Question();
        question.setQuestion(requestDTO.getQuestion());
        question.setImage(requestDTO.getImage());
        question.setCreatedAt(LocalDateTime.now());
        return question;
    }
}
