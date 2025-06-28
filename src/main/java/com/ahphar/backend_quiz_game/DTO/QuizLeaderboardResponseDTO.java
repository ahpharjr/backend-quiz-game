package com.ahphar.backend_quiz_game.DTO;

import lombok.*;

@Getter
@Setter
public class QuizLeaderboardResponseDTO {
    
    private String username;
    private int point;
    private Long timeTaken;
    private String profilePicture;

}
