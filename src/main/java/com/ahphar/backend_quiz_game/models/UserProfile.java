package com.ahphar.backend_quiz_game.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    private UUID userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private int level;
    private int userXp;
    private long timeSpent;
    private int quizSet;
    private int highestScore;
    private String profilePicture;
    // private int currentQuizSet;
    private int currentPhase;

}
