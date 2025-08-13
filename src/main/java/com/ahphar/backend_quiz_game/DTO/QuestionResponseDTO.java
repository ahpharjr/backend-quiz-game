package com.ahphar.backend_quiz_game.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponseDTO {
    private Long questionId;
    private String questionText;
    private String image;
}
