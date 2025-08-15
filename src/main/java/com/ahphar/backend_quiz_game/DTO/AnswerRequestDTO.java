package com.ahphar.backend_quiz_game.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnswerRequestDTO {
    
    @NotBlank(message = "Answer text is required.")
    private String answer;

    @NotNull(message = "Correctness status is required.")
    private Boolean isCorrect;
}
