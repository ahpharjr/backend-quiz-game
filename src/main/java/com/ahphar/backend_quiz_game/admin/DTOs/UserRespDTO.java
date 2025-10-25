package com.ahphar.backend_quiz_game.admin.DTOs;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRespDTO {
    private UUID id;
    private String username;
    private String email;
    private boolean isVerified;
    private String role;
    private LocalDateTime createdAt;
    
    private int level;
    private int xp;
    private long timeSpent;
    private int quizSet;
    private int highestScore;
    private String profilePicture;
    private int currentPhase;
}
