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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/phases")
@Tag(name = "Admin Phase Management", description = "APIs for managing phases in the quiz game")
@RequiredArgsConstructor
public class AdminPhaseController {

    private final PhaseService phaseService;


    
    @Operation(
        summary = "Get all phases", 
        description = "Retrieve a list of all phases in the quiz game",
        security = @SecurityRequirement(name = "bearerAuth")
        )
    @GetMapping
    public ResponseEntity<List<Phase>> getAllPhases() {
        List<Phase> phases = phaseService.getAllPhases();
        return ResponseEntity.ok(phases);
    }

    @Operation(
        summary = "Create a new phase",
        description = "Creates a new phase in the quiz game. Requires admin privileges.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping
    public ResponseEntity<MessageResponse> createPhase(@Valid @RequestBody PhaseRequestDTO requestDto) {
        
        phaseService.createPhase(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Phase created successfully"));
    }

    @Operation(
        summary = "Update an existing phase",
        description = "Updates the details of an existing phase. Requires admin privileges.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/{phaseId}")
    public ResponseEntity<MessageResponse> updatePhase(@PathVariable Long phaseId, @Valid @RequestBody PhaseRequestDTO requestDto) {
        
        phaseService.updatePhase(phaseId, requestDto);
        return ResponseEntity.ok(new MessageResponse("Phase updated successfully"));
    }

    @Operation(
        summary = "Delete a phase",
        description = "Deletes an existing phase and all the related topics from the quiz game. Requires admin privileges.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/{phaseId}")
    public ResponseEntity<MessageResponse> deletePhase(@PathVariable Long phaseId) {
        
        phaseService.deletePhase(phaseId);
        return ResponseEntity.ok(new MessageResponse("Phase deleted successfully"));
    }


}
