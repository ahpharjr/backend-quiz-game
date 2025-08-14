package com.ahphar.backend_quiz_game.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FlashcardRequestDTO {
    
    @NotBlank(message = "Keyword is required.")
    private String keyword;

    @NotBlank(message = "Definition is required.")
    private String definition;

    @NotBlank(message = "Image is required.")
    private String image;
}
