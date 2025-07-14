package com.ahphar.backend_quiz_game.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
public class TopicRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Image is required")
    private String image;

    @NotBlank(message = "Description is required")
    private String desc;

    @NotNull(message = "Phase ID is required")
    private Long phaseId; 
    
}
