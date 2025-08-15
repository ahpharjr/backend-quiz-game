package com.ahphar.backend_quiz_game.mapper;

import org.springframework.stereotype.Component;

import com.ahphar.backend_quiz_game.DTO.AnswerRequestDTO;
import com.ahphar.backend_quiz_game.DTO.AnswerResponseDTO;
import com.ahphar.backend_quiz_game.models.Answer;

@Component
public class AnswerMapper {
    
    public AnswerResponseDTO toDto(Answer answer){
        AnswerResponseDTO dto = new AnswerResponseDTO();
        dto.setAnswerId(answer.getAnswerId());
        dto.setAnswer(answer.getAnswer());
        dto.setCorrect(answer.isCorrect());
        dto.setQuestionId(answer.getQuestion().getQuestionId());

        return dto;
    }

    public Answer toModel(AnswerRequestDTO dto){
        Answer answer = new Answer();
        answer.setAnswer(dto.getAnswer());
        answer.setCorrect(dto.getIsCorrect());
        return answer;
    }
}
