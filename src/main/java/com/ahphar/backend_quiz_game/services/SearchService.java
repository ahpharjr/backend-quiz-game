package com.ahphar.backend_quiz_game.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ahphar.backend_quiz_game.models.Flashcard;
import com.ahphar.backend_quiz_game.repositories.FlashcardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final FlashcardRepository flashcardRepository;

    public List<String> getSuggestions(String term) {
        return flashcardRepository.findKeywordsByFlexibleSearch(term);
    }

    public List<Flashcard> searchFlashcards(String keyword) {
        return flashcardRepository.searchFlashcards(keyword);
    }
}
