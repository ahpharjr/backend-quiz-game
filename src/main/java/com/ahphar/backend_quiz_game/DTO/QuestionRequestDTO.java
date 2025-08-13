package com.ahphar.backend_quiz_game.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QuestionRequestDTO {

    @NotBlank(message = "Question Text is required")
    private String question;

    @NotBlank(message = "Question Image is required")
    private String image;
}
