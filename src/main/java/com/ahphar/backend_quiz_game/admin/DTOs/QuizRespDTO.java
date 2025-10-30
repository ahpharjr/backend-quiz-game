package com.ahphar.backend_quiz_game.admin.DTOs;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizRespDTO {
    
    private Long quizId;
    private String name;
    private String image;
    private String desc;
    private LocalDateTime createdAt;
    private Long topicId;
}
