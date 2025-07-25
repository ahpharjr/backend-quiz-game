package com.ahphar.backend_quiz_game.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahphar.backend_quiz_game.DTO.PhaseRequestDTO;
import com.ahphar.backend_quiz_game.mapper.PhaseMapper;
import com.ahphar.backend_quiz_game.models.Phase;
import com.ahphar.backend_quiz_game.repositories.PhaseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PhaseService {
    
    private final PhaseRepository phaseRepository;
    private final PhaseMapper phaseMapper;

    public List<Phase> getAllPhases() {
        return phaseRepository.findAll();
    }

    public Phase getPhaseById(Long phaseId) {
        return phaseRepository.findById(phaseId)
                .orElseThrow(() -> new RuntimeException("Phase not found with id: " + phaseId));
    }

    public void createPhase(PhaseRequestDTO requestDto) {
        Phase phase = phaseMapper.toModel(requestDto);

        phaseRepository.save(phase);
    }

    public void updatePhase(Long phaseId, PhaseRequestDTO requestDto) {
        Phase existingPhase = getPhaseById(phaseId);
        Phase updatedPhase = phaseMapper.toModel(requestDto);

        updatedPhase.setPhaseId(existingPhase.getPhaseId());
        phaseRepository.save(updatedPhase);
    }

    @Transactional
    public void deletePhase(Long phaseId){
        Phase existingPhase = getPhaseById(phaseId);
        phaseRepository.delete(existingPhase);
    }
}
