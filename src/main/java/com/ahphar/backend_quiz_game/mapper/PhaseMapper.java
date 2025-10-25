package com.ahphar.backend_quiz_game.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.ahphar.backend_quiz_game.DTO.PhaseRequestDTO;
import com.ahphar.backend_quiz_game.DTO.PhaseResponseDTO;
import com.ahphar.backend_quiz_game.models.Phase;

@Component
public class PhaseMapper {
    
    public Phase toModel(PhaseRequestDTO requestDto) {
        Phase phase = new Phase();
        phase.setName(requestDto.getName());
        phase.setImage(requestDto.getImage());
        phase.setDesc(requestDto.getDesc());
        phase.setCreatedAt(LocalDateTime.now());

        return phase;
    }

    public PhaseResponseDTO toDto(Phase phase){
        PhaseResponseDTO dto = new PhaseResponseDTO();
        dto.setPhaseId(phase.getPhaseId());
        dto.setName(phase.getName());
        dto.setImage(phase.getImage());
        dto.setDesc(phase.getDesc());
        dto.setCreatedAt(phase.getCreatedAt());

        return dto;
    }
}
