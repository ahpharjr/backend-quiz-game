package com.ahphar.backend_quiz_game.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ahphar.backend_quiz_game.DTO.PhaseResponseDTO;
import com.ahphar.backend_quiz_game.services.PhaseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/phases")
@Tag(name = "Phases", description = "Endpoints for retrieving quiz phases")
@RequiredArgsConstructor
public class PhaseController {

    private final PhaseService phaseService;

    @Operation(
        summary = "Get all phases",
        description = "Retrieves all quiz game phases from the database. Each phase may contain multiple topics and quizzes.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping
    public ResponseEntity<List<PhaseResponseDTO>> getAllPhases(){

        List<PhaseResponseDTO> phases = phaseService.getAllPhases();

        return ResponseEntity.ok(phases);
    }

    @GetMapping("/{phaseId}")
    public ResponseEntity<PhaseResponseDTO> getPhase(@PathVariable Long phaseId){
        PhaseResponseDTO phase = phaseService.getPhase(phaseId);

        return ResponseEntity.ok(phase);
    }
}
