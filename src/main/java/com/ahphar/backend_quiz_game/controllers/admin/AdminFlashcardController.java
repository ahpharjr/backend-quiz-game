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

import com.ahphar.backend_quiz_game.DTO.FlashcardRequestDTO;
import com.ahphar.backend_quiz_game.DTO.FlashcardResponseDTO;
import com.ahphar.backend_quiz_game.DTO.MessageResponse;
import com.ahphar.backend_quiz_game.services.FlashcardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Flashcard Management", description = "APIs for managing flashcards in the admin panel")
public class AdminFlashcardController {

    private final FlashcardService flashcardService;
    
    @Operation(
        summary = "Get all flashcards",
        description = "Retrieve a list of all flashcards",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/flashcards")
    public ResponseEntity<List<FlashcardResponseDTO>> getAllFlashcards(){
        List<FlashcardResponseDTO> flashcards = flashcardService.getAllFlashcards();

        return ResponseEntity.ok(flashcards);
    }

    @Operation(
        summary = "Get flashcards by topic ID",
        description = "Retrieve a list of flashcards associated with a specific topic",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/topics/{topicId}/flashcards")
    public ResponseEntity<List<FlashcardResponseDTO>> getFlashcardsByTopicId(@PathVariable long topicId){
        List<FlashcardResponseDTO> flashcards = flashcardService.getFlashcardsByTopicId(topicId);

        return ResponseEntity.ok(flashcards);
    }

    @Operation(
        summary = "Create a new flashcard",
        description = "Create a new flashcard associated with a specific topic",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/topics/{topicId}/flashcard")
    public ResponseEntity<MessageResponse> createFlashcard(@PathVariable Long topicId,@Valid @RequestBody FlashcardRequestDTO requestDTO){
        flashcardService.createFlashcard(topicId, requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageResponse("Created new flashcard successfully!"));
    }

    @Operation(
        summary = "Update an existing flashcard",
        description = "Update an existing flashcard by its ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/topics/{topicId}/flashcards/{flashcardId}")
    public ResponseEntity<MessageResponse> updateFlashcard(@PathVariable long flashcardId,@PathVariable Long topicId,@Valid @RequestBody FlashcardRequestDTO requestDTO){
        flashcardService.updateFlashcard(flashcardId, topicId, requestDTO);

        return ResponseEntity.ok(new MessageResponse("Flashcard updated successfully!"));
    }

    @Operation(
        summary = "Delete an existing flashcard",
        description = "Delete an existing flashcard by its ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/topics/{topicId}/flashcards/{flashcardId}")
    public ResponseEntity<MessageResponse> deleteFlashcard(@PathVariable Long flashcardId, @PathVariable Long topicId){
        flashcardService.deleteFlashcard(flashcardId, topicId);

        return ResponseEntity.ok(new MessageResponse("Flashcard deleted successfully!"));
    }
}
