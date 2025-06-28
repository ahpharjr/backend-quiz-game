package com.ahphar.backend_quiz_game.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmitRequestDTO {

    @NotNull(message = "Point is required")
    private Integer point;

    @NotNull(message = "TimeTaken is required")
    private Long timeTaken;

    @NotNull(message = "Correct percentage is required")
    private Double correctPercentage;
    
}

