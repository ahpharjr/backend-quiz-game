package com.ahphar.backend_quiz_game.services;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahphar.backend_quiz_game.DTO.PhaseRequestDTO;
import com.ahphar.backend_quiz_game.DTO.PhaseResponseDTO;
import com.ahphar.backend_quiz_game.mapper.PhaseMapper;
import com.ahphar.backend_quiz_game.models.Phase;
import com.ahphar.backend_quiz_game.repositories.PhaseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PhaseService {
    
    private final PhaseRepository phaseRepository;
    private final PhaseMapper phaseMapper;

    @Cacheable("phases")
    public List<PhaseResponseDTO> getAllPhases(){
        List<Phase> phases = phaseRepository.findAll();
        return phases.stream()
                    .map(phaseMapper::toDto)
                    .toList();
    }

    public Phase getPhaseById(Long phaseId) {
        return phaseRepository.findById(phaseId)
                .orElseThrow(() -> new RuntimeException("Phase not found with id: " + phaseId));
    }

    @CacheEvict(value = "phases", allEntries = true)
    public void createPhase(PhaseRequestDTO requestDto) {
        Phase phase = phaseMapper.toModel(requestDto);

        phaseRepository.save(phase);
    }

    @CacheEvict(value = "phases", allEntries = true)
    public void updatePhase(Long phaseId, PhaseRequestDTO requestDto) {
        Phase existingPhase = getPhaseById(phaseId);
        Phase updatedPhase = phaseMapper.toModel(requestDto);

        updatedPhase.setPhaseId(existingPhase.getPhaseId());
        phaseRepository.save(updatedPhase);
    }

    @CacheEvict(value = "phases", allEntries = true)
    @Transactional
    public void deletePhase(Long phaseId){
        Phase existingPhase = getPhaseById(phaseId);
        phaseRepository.delete(existingPhase);
    }
}
