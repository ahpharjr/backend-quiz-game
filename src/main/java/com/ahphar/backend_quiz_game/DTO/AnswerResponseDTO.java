package com.ahphar.backend_quiz_game.DTO;

import lombok.Data;

@Data
public class AnswerResponseDTO {
    
    private Long answerId;
    private String answer;
    private boolean isCorrect;
    private Long questionId;
    
}
