package com.ahphar.backend_quiz_game.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ahphar.backend_quiz_game.models.Flashcard;
import com.ahphar.backend_quiz_game.services.SearchService;

@RestController
@RequestMapping("/search")
public class SearchController {
    private final SearchService searchService;
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    // Suggestions endpoint
    @GetMapping("/suggestions")
    public ResponseEntity<List<String>> getKeywordSuggestions(@RequestParam String term) {
        List<String> suggestions = searchService.getSuggestions(term);
        return new ResponseEntity<>(suggestions, HttpStatus.OK);
    }

    // Search result endpoint
    @GetMapping
    public ResponseEntity<List<Flashcard>> searchFlashcards(@RequestParam String keyword) {
        List<Flashcard> flashcards = searchService.searchFlashcards(keyword);
        return new ResponseEntity<>(flashcards, HttpStatus.OK);
    }
}
