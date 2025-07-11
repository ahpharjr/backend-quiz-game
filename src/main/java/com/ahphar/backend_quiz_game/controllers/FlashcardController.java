package com.ahphar.backend_quiz_game.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ahphar.backend_quiz_game.services.FlashcardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.ahphar.backend_quiz_game.models.*;

@RestController
@RequestMapping("/topics")
@Tag(name = "Flashcards", description = "Operations related to flashcards under specific topics")
public class FlashcardController {

    private final FlashcardService flashcardService;
    public FlashcardController(final FlashcardService flashcardService) {
        this.flashcardService = flashcardService;
    }

    @Operation(
        summary = "Get flashcards by topic ID",
        description = "Returns a list of all flashcards associated with the given topic ID.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{topicId}/flashcards")
    public ResponseEntity<List<Flashcard>> getFlashcards(@PathVariable int topicId) {
        List<Flashcard> flashcards = flashcardService.getAllFlashcards(topicId);

        return new ResponseEntity<>(flashcards, HttpStatus.OK);
    }


}
