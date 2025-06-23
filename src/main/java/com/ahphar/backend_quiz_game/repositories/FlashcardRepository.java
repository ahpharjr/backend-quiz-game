package com.ahphar.backend_quiz_game.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ahphar.backend_quiz_game.models.Flashcard;

import java.util.List;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {
    List<Flashcard> findByTopic_TopicId(long topicId);
    Flashcard findByCardId(long flashcardId);

    @Query("SELECT f FROM Flashcard f WHERE LOWER(f.keyword) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Flashcard> searchFlashcards(@Param("searchTerm") String searchTerm);

    @Query("SELECT DISTINCT f.keyword FROM Flashcard f " +
            "WHERE LOWER(f.keyword) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "ORDER BY LENGTH(f.keyword), f.keyword ASC")
    List<String> findKeywordsByFlexibleSearch(@Param("term") String term);

}