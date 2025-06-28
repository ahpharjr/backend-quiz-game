package com.ahphar.backend_quiz_game.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {

    private String username;
    private String email;

    //Profile fields
    private int level;
    private int userXp;
    private long timeSpent;
    private int quizSet;
    private int highestScore;
    private String profilePicture;
    // private int currentQuizSet;
    private int currentPhase;

}
