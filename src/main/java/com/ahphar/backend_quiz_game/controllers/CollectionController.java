package com.ahphar.backend_quiz_game.controllers;

import com.ahphar.backend_quiz_game.DTO.CollectionRequestDTO;
import com.ahphar.backend_quiz_game.models.Collection;
import com.ahphar.backend_quiz_game.models.Flashcard;
import com.ahphar.backend_quiz_game.models.User;
import com.ahphar.backend_quiz_game.services.CollectionService;
import com.ahphar.backend_quiz_game.services.FlashcardService;
import com.ahphar.backend_quiz_game.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collections")
public class CollectionController {

    private final CollectionService collectionService;
    private final UserService userService;
    private final FlashcardService flashcardService;

    public CollectionController(CollectionService collectionService, UserService userService, FlashcardService flashcardService) {
        this.collectionService = collectionService;
        this.userService = userService;
        this.flashcardService = flashcardService;
    }

    @GetMapping
    public ResponseEntity<List<Collection>> getFlashcardCollection(Authentication auth) {
        String username = auth.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
        List<Collection> collections = collectionService.getFlashcardCollections(user.getUserId());

        return new ResponseEntity<>(collections, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCardToCollection(Authentication auth, @RequestBody CollectionRequestDTO requestDTO) {
        String username = auth.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));

        long flashcardId = requestDTO.getFlashcardId();
        Flashcard flashcard = flashcardService.getFlashcard(flashcardId);

        List<Collection> existingCollections = collectionService.getFlashcardCollections(user.getUserId());
//        boolean alreadyInCollection = existingCollections.stream()
//                .anyMatch(c -> c.getFlashcard().getCardId() == (flashcardId));

        boolean alreadyInCollection = collectionService.isCardInCollection(existingCollections, flashcardId);
        if(alreadyInCollection) {
            return ResponseEntity.badRequest().body("Flashcard is already in collection");
        }

        collectionService.createCollection(user, flashcard);

        return ResponseEntity.ok("Card added to collection successfully");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeCardFromCollection(Authentication auth, @RequestBody CollectionRequestDTO requestDTO) {
        String username = auth.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));

        long flashcardId = requestDTO.getFlashcardId();

        boolean removed = collectionService.removeCollection(user.getUserId(), flashcardId);
        if(!removed) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Flashcard not found");
        }

        return ResponseEntity.ok("Flashcard removed from collection successfully");
    }

}
