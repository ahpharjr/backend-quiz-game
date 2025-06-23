package com.ahphar.backend_quiz_game.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quizzes")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizId;
    
    @OneToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;
}
