package com.ahphar.backend_quiz_game.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PhaseRequestDTO {
    
    @NotBlank(message = "Phase name is required")
    private String name;

    @NotBlank(message = "Phase image is required")
    private String image;

    @NotBlank(message = "Phase description is required")
    private String desc;
    
}
