package com.ahphar.backend_quiz_game.mapper;

import org.springframework.stereotype.Component;

import com.ahphar.backend_quiz_game.DTO.PhaseRequestDTO;
import com.ahphar.backend_quiz_game.models.Phase;

@Component
public class PhaseMapper {
    
    public Phase toModel(PhaseRequestDTO requestDto) {
        Phase phase = new Phase();
        phase.setName(requestDto.getName());
        phase.setImage(requestDto.getImage());
        phase.setDesc(requestDto.getDesc());

        return phase;
    }
}
