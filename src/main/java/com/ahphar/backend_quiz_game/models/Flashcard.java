package com.ahphar.backend_quiz_game.models;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "flashcards")
public class Flashcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cardId;

    private String keyword;
    private String definition;
    private String image;
    private LocalDateTime createAt;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

}
