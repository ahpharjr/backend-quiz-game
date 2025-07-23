package com.ahphar.backend_quiz_game.mapper;

import org.springframework.stereotype.Component;

import com.ahphar.backend_quiz_game.DTO.FlashcardResponseDTO;
import com.ahphar.backend_quiz_game.models.Flashcard;

@Component
public class FlahscardMapper {
    
    public FlashcardResponseDTO toDto(Flashcard flashcard){
        FlashcardResponseDTO dto = new FlashcardResponseDTO();
        dto.setCardId(flashcard.getCardId());
        dto.setKeyword(flashcard.getKeyword());
        dto.setDefinition(flashcard.getDefinition());
        dto.setImage(flashcard.getImage());

        return dto;
    }
}
