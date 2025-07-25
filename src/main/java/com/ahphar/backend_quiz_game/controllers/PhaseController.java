package com.ahphar.backend_quiz_game.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ahphar.backend_quiz_game.models.Phase;
import com.ahphar.backend_quiz_game.repositories.PhaseRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/phases")
@Tag(name = "Phases", description = "Endpoints for retrieving quiz phases")
@RequiredArgsConstructor
public class PhaseController {

    private final PhaseRepository phaseRepository;
    
    @Operation(
        summary = "Get all phases",
        description = "Retrieves all quiz game phases from the database. Each phase may contain multiple topics and quizzes.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping
    public ResponseEntity<List<Phase>> getAllPhases(){
        List<Phase> phases = phaseRepository.findAll();
        return ResponseEntity.ok(phases);
    }
}
