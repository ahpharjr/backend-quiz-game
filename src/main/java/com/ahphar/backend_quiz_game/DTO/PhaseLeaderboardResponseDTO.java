package com.ahphar.backend_quiz_game.DTO;

import java.io.Serializable;

import lombok.*;

@Getter
@Setter
public class PhaseLeaderboardResponseDTO implements Serializable{

    private String username;
    private int point;
    private Long timeTaken;
    private String profilePicture;
    
}
