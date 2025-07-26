package com.ahphar.backend_quiz_game.controllers;

import com.ahphar.backend_quiz_game.DTO.CollectionRequestDTO;
import com.ahphar.backend_quiz_game.DTO.FlashcardResponseDTO;
import com.ahphar.backend_quiz_game.models.Collection;
import com.ahphar.backend_quiz_game.models.Flashcard;
import com.ahphar.backend_quiz_game.models.User;
import com.ahphar.backend_quiz_game.services.CollectionService;
import com.ahphar.backend_quiz_game.services.FlashcardService;
import com.ahphar.backend_quiz_game.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collections")
@Tag(name = "Collections", description = "Manage flashcard collections for the authenticated user")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;
    private final UserService userService;
    private final FlashcardService flashcardService;

    @Operation(
        summary = "Get flashcard collection",
        description = "Retrieves all flashcards saved in the current user's collection.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<FlashcardResponseDTO>> getFlashcardCollection(Authentication auth) {
        System.out.println("Fetching flashcard collection for user");
        User user = userService.getCurrentUser(auth);
        List<FlashcardResponseDTO> collections = collectionService.getCollections(user.getUserId());

        System.out.println("Returning flashcard collection for user: " + collections);
        return new ResponseEntity<>(collections, HttpStatus.OK);
    }

    @Operation(
        summary = "Add flashcard to collection",
        description = "Adds a flashcard to the authenticated user's collection. If the card is already added, a 400 response is returned.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping()
    public ResponseEntity<String> addCardToCollection(Authentication auth, @RequestBody CollectionRequestDTO requestDTO) {
        User user = userService.getCurrentUser(auth);

        long flashcardId = requestDTO.getFlashcardId();
        Flashcard flashcard = flashcardService.getFlashcard(flashcardId);

        List<Collection> existingCollections = collectionService.getFlashcardCollections(user.getUserId());

        boolean alreadyInCollection = collectionService.isCardInCollection(existingCollections, flashcardId);
        if(alreadyInCollection) {
            return ResponseEntity.badRequest().body("Flashcard is already in collection");
        }

        collectionService.createCollection(user, flashcard);

        return ResponseEntity.ok("Card added to collection successfully");
    }

    @Operation(
        summary = "Remove flashcard from collection",
        description = "Removes a flashcard from the authenticated user's collection by flashcard ID.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping()
    public ResponseEntity<String> removeCardFromCollection(Authentication auth, @RequestBody CollectionRequestDTO requestDTO) {
        User user = userService.getCurrentUser(auth);

        long flashcardId = requestDTO.getFlashcardId();

        boolean removed = collectionService.removeCollection(user.getUserId(), flashcardId);
        if(!removed) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Flashcard not found");
        }

        return ResponseEntity.ok("Flashcard removed from collection successfully");
    }

}
