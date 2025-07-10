package com.ahphar.backend_quiz_game.mapper;

import org.springframework.stereotype.Component;

import com.ahphar.backend_quiz_game.DTO.AchievementResponseDTO;
import com.ahphar.backend_quiz_game.models.Achievement;

@Component
public class AchievementMapper {
    
    public AchievementResponseDTO toDto(Achievement achievement){
        AchievementResponseDTO dto = new AchievementResponseDTO();

        dto.setTitle(achievement.getTitle());
        dto.setImageUrl(achievement.getImageUrl());
        dto.setDescription(achievement.getDescription());

        return dto;
    }
}
