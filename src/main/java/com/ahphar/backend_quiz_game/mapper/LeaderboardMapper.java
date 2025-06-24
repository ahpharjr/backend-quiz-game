package com.ahphar.backend_quiz_game.mapper;

import org.springframework.stereotype.Component;

import com.ahphar.backend_quiz_game.DTO.PhaseLeaderboardResponseDTO;
import com.ahphar.backend_quiz_game.DTO.QuizLeaderboardResponseDTO;
import com.ahphar.backend_quiz_game.models.PhaseLeaderboard;
import com.ahphar.backend_quiz_game.models.QuizLeaderboard;

@Component
public class LeaderboardMapper {
    
    public PhaseLeaderboardResponseDTO toDto(PhaseLeaderboard phaseLeaderboard) {
        PhaseLeaderboardResponseDTO dto = new PhaseLeaderboardResponseDTO();

        dto.setUsername(phaseLeaderboard.getUser().getUsername());
        dto.setPoint(phaseLeaderboard.getPoint());
        dto.setTimeTaken(phaseLeaderboard.getTimeTaken());
        dto.setProfilePicture(phaseLeaderboard.getUser().getProfile().getProfilePicture());

        return dto;
    }

        public QuizLeaderboardResponseDTO toDto(QuizLeaderboard quizLeaderboard) {
        QuizLeaderboardResponseDTO dto = new QuizLeaderboardResponseDTO();

        dto.setUsername(quizLeaderboard.getUser().getUsername());
        dto.setPoint(quizLeaderboard.getPoint());
        dto.setTimeTaken(quizLeaderboard.getTimeTaken());
        dto.setProfilePicture(quizLeaderboard.getUser().getProfile().getProfilePicture());

        return dto;
    }


}
