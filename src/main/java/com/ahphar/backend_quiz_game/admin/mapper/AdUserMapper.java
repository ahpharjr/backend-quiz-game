package com.ahphar.backend_quiz_game.admin.mapper;

import org.springframework.stereotype.Component;

import com.ahphar.backend_quiz_game.admin.DTOs.UserRespDTO;
import com.ahphar.backend_quiz_game.models.User;
import com.ahphar.backend_quiz_game.models.UserProfile;

@Component
public class AdUserMapper {

    public UserRespDTO toDto(User user) {
        UserRespDTO dto = new UserRespDTO();

        dto.setId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setVerified(user.isVerified());
        dto.setRole(user.getRole().name());
        dto.setCreatedAt(user.getRegisteredTime());

        UserProfile profile = user.getProfile();

        if (profile != null) {
            dto.setLevel(profile.getLevel());
            dto.setXp(profile.getUserXp());
            dto.setTimeSpent(profile.getTimeSpent());
            dto.setQuizSet(profile.getQuizSet());
            dto.setHighestScore(profile.getHighestScore());
            dto.setProfilePicture(profile.getProfilePicture());
            dto.setCurrentPhase(profile.getCurrentPhase());
        }

        return dto;
    }
}
