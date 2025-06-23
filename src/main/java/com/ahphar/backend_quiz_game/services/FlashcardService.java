package com.ahphar.backend_quiz_game.services;

import java.util.List;
import org.springframework.stereotype.Service;

import com.ahphar.backend_quiz_game.models.Flashcard;
import com.ahphar.backend_quiz_game.repositories.FlashcardRepository;

@Service
public class FlashcardService {
    private final FlashcardRepository flashcardRepository;


    public FlashcardService(FlashcardRepository flashcardRepository) {
        this.flashcardRepository = flashcardRepository;
    }

    public List<Flashcard> getAllFlashcards(long topicId) {
        return flashcardRepository.findByTopic_TopicId(topicId);
    }

    public Flashcard getFlashcard(long flashcardId){
        return flashcardRepository.findByCardId(flashcardId);
    }

}