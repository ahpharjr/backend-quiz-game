package com.ahphar.backend_quiz_game.DTO;

import java.io.Serializable;

import lombok.*;

@Data
@Getter
@Setter
public class PhaseResponseDTO implements Serializable{
    
    private Long phaseId;
    private String name;
    private String image;
    private String desc;

}
