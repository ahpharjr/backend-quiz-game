package com.ahphar.backend_quiz_game.DTO;

import lombok.Data;

@Data
public class FlashcardResponseDTO {
    
    private String keyword;
    private String definition;
    private String image;
}
