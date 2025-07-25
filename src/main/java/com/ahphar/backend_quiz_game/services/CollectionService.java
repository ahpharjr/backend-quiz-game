package com.ahphar.backend_quiz_game.services;

import com.ahphar.backend_quiz_game.DTO.FlashcardResponseDTO;
import com.ahphar.backend_quiz_game.mapper.FlahscardMapper;
import com.ahphar.backend_quiz_game.models.Collection;
import com.ahphar.backend_quiz_game.models.Flashcard;
import com.ahphar.backend_quiz_game.models.User;
import com.ahphar.backend_quiz_game.repositories.CollectionRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final FlahscardMapper flahscardMapper;

    public List<Collection> getFlashcardCollections(UUID userId){
        return collectionRepository.findAllByUser_UserId(userId);
    }

    public List<FlashcardResponseDTO> getCollections(UUID userId){
        return collectionRepository.findAllByUser_UserId(userId).stream()
                .map(c -> flahscardMapper.toDto(c.getFlashcard()))
                .toList();
    }

    public boolean isCardInCollection(List<Collection> existingCollection, long cardId){
        return existingCollection.stream()
                .anyMatch(c -> c.getFlashcard().getCardId() == (cardId));
    }

    public void createCollection(User user, Flashcard flashcard){
        Collection collection = new Collection();
        collection.setUser(user);
        collection.setFlashcard(flashcard);
        collectionRepository.save(collection);
    }

    public boolean removeCollection(UUID userId, long flashcardId){
        List<Collection> collections = collectionRepository.findAllByUser_UserId(userId);
        for(Collection c : collections){
            if(c.getFlashcard().getCardId() == flashcardId){
                collectionRepository.delete(c);
                return true;
            }
        }
        return false;
    }
}
