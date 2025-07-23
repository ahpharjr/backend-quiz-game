package com.ahphar.backend_quiz_game.DTO;

import java.io.Serializable;

import lombok.Data;

@Data
public class FlashcardResponseDTO implements Serializable{
    
    private long cardId;
    private String keyword;
    private String definition;
    private String image;
}
