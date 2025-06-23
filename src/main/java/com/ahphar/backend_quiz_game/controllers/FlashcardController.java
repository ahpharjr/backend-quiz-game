package com.ahphar.backend_quiz_game.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ahphar.backend_quiz_game.services.FlashcardService;
import com.ahphar.backend_quiz_game.models.*;

@RestController
@RequestMapping("/topics")
public class FlashcardController {

    private final FlashcardService flashcardService;
    public FlashcardController(final FlashcardService flashcardService) {
        this.flashcardService = flashcardService;
    }

    @GetMapping("/{topicId}/flashcards")
    public ResponseEntity<List<Flashcard>> getFlashcards(@PathVariable int topicId) {
        List<Flashcard> flashcards = flashcardService.getAllFlashcards(topicId);

        return new ResponseEntity<>(flashcards, HttpStatus.OK);
    }


}
