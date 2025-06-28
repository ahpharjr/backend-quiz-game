package com.ahphar.backend_quiz_game.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    
    private int userXp;
    private int level;
    private long timeSpent;
    private int quizSet;
    private int highestScore;
    private int currentPhase;
}

