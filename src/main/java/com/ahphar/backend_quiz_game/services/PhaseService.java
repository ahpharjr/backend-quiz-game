package com.ahphar.backend_quiz_game.services;

import org.springframework.stereotype.Service;

import com.ahphar.backend_quiz_game.models.Phase;
import com.ahphar.backend_quiz_game.repositories.PhaseRepository;

@Service
public class PhaseService {
    
    private final PhaseRepository phaseRepository;

    public PhaseService(PhaseRepository phaseRepository) {
        this.phaseRepository = phaseRepository;
    }

    public Phase getPhaseById(Long phaseId) {
        return phaseRepository.findById(phaseId)
                .orElseThrow(() -> new RuntimeException("Phase not found with id: " + phaseId));
    }
}
