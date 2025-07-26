package com.ahphar.backend_quiz_game.services;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ahphar.backend_quiz_game.DTO.FlashcardResponseDTO;
import com.ahphar.backend_quiz_game.mapper.FlahscardMapper;
import com.ahphar.backend_quiz_game.models.Flashcard;
import com.ahphar.backend_quiz_game.repositories.FlashcardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlashcardService {
    private final FlashcardRepository flashcardRepository;
    private final FlahscardMapper flashcardMapper;

    public List<Flashcard> getAllFlashcards(long topicId) {
        return flashcardRepository.findByTopic_TopicId(topicId);
    }

    @Cacheable(value="flashcards", key="#topicId")
    public List<FlashcardResponseDTO> getFlashcardsByTopicId(long topicId){
        List<Flashcard> flashcards = flashcardRepository.findByTopic_TopicId(topicId);

        return flashcards.stream()
                .map(flashcardMapper::toDto)
                .toList();
            
    }

    public Flashcard getFlashcard(long flashcardId){
        return flashcardRepository.findByCardId(flashcardId);
    }

}