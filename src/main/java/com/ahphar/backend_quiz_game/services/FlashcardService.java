package com.ahphar.backend_quiz_game.services;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ahphar.backend_quiz_game.DTO.FlashcardRequestDTO;
import com.ahphar.backend_quiz_game.DTO.FlashcardResponseDTO;
import com.ahphar.backend_quiz_game.mapper.FlahscardMapper;
import com.ahphar.backend_quiz_game.models.Flashcard;
import com.ahphar.backend_quiz_game.repositories.FlashcardRepository;
import com.ahphar.backend_quiz_game.exception.FlashcardNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlashcardService {

    private final FlashcardRepository flashcardRepository;
    private final FlahscardMapper flashcardMapper;
    private final TopicService topicService;

    public List<Flashcard> getAllFlashcards(long topicId) {
        return flashcardRepository.findByTopic_TopicId(topicId);
    }

    public Flashcard getFlashcard(long flashcardId){
        return flashcardRepository.findByCardId(flashcardId);
    }

    @Cacheable(value="flashcards", key="#topicId")
    public List<FlashcardResponseDTO> getFlashcardsByTopicId(long topicId){
        List<Flashcard> flashcards = flashcardRepository.findByTopic_TopicId(topicId);

        return flashcards.stream()
                .map(flashcardMapper::toDto)
                .toList();
            
    }

    public List<FlashcardResponseDTO> getAllFlashcards(){
        List<Flashcard> flashcards = flashcardRepository.findAll();
        return flashcards.stream()
                .map(flashcardMapper::toDto)
                .toList();
    }

    @CacheEvict(value = "flashcards", key = "#topicId")
    public void createFlashcard(Long topicId,FlashcardRequestDTO dto){
        Flashcard flashcard = flashcardMapper.toModel(dto);
        flashcard.setTopic(topicService.getTopicById(topicId));
        flashcardRepository.save(flashcard);
    }

    @CacheEvict(value = "flashcards", key = "#topicId")
    public void updateFlashcard(Long flashcardId, Long topicId, FlashcardRequestDTO dto){
        Flashcard flashcard = flashcardRepository.findById(flashcardId)
                                .orElseThrow(()-> new FlashcardNotFoundException("Flashcard not found with id: "+ flashcardId));

        flashcard.setKeyword(dto.getKeyword());
        flashcard.setDefinition(dto.getDefinition());
        flashcard.setImage(dto.getImage());

        flashcardRepository.save(flashcard);
    }

    @CacheEvict(value = "flashcards", key = "#topicId")
    public void deleteFlashcard(Long flashcardId, Long topicId){
        Flashcard flashcard = flashcardRepository.findById(flashcardId)
                .orElseThrow(() -> new FlashcardNotFoundException("Flashcard not found with id: " + flashcardId));

        flashcardRepository.delete(flashcard);
    }

}