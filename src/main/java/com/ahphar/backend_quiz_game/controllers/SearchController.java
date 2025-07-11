package com.ahphar.backend_quiz_game.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ahphar.backend_quiz_game.models.Flashcard;
import com.ahphar.backend_quiz_game.services.SearchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/search")
@Tag(name = "Search", description ="Endpoints for searching flashcards")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @Operation(
        summary = "Get keyword suggestions",
        description = "Returns a list of suggested keywords based on the provided partial term. Useful for autocomplete features.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/suggestions")
    public ResponseEntity<List<String>> getKeywordSuggestions(@RequestParam String term) {
        List<String> suggestions = searchService.getSuggestions(term);
        return new ResponseEntity<>(suggestions, HttpStatus.OK);
    }

    @Operation(
        summary = "Search flashcards by keyword",
        description = "Returns a list of flashcards that match the provided keyword.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping
    public ResponseEntity<List<Flashcard>> searchFlashcards(@RequestParam String keyword) {
        List<Flashcard> flashcards = searchService.searchFlashcards(keyword);
        return new ResponseEntity<>(flashcards, HttpStatus.OK);
    }
}
