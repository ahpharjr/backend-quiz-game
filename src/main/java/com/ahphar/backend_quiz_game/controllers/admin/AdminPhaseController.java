package com.ahphar.backend_quiz_game.controllers.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ahphar.backend_quiz_game.DTO.MessageResponse;
import com.ahphar.backend_quiz_game.DTO.PhaseRequestDTO;
import com.ahphar.backend_quiz_game.models.Phase;
import com.ahphar.backend_quiz_game.services.PhaseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin/phases")
public class AdminPhaseController {

    private final PhaseService phaseService;

    public AdminPhaseController(PhaseService phaseService) {
        this.phaseService = phaseService;
    }
    
    @GetMapping
    public ResponseEntity<List<Phase>> getAllPhases() {
        List<Phase> phases = phaseService.getAllPhases();
        return ResponseEntity.ok(phases);
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createPhase(@Valid @RequestBody PhaseRequestDTO requestDto) {
        
        phaseService.createPhase(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Phase created successfully"));
    }

    @PutMapping("/{phaseId}")
    public ResponseEntity<MessageResponse> updatePhase(@PathVariable Long phaseId, @Valid @RequestBody PhaseRequestDTO requestDto) {
        
        phaseService.updatePhase(phaseId, requestDto);
        return ResponseEntity.ok(new MessageResponse("Phase updated successfully"));
    }

    @DeleteMapping("/{phaseId}")
    public ResponseEntity<MessageResponse> deletePhase(@PathVariable Long phaseId) {
        
        phaseService.deletePhase(phaseId);
        return ResponseEntity.ok(new MessageResponse("Phase deleted successfully"));
    }


}
