package com.ahphar.backend_quiz_game.repositories;

import com.ahphar.backend_quiz_game.models.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {
    List<Collection> findAllByUser_UserId(UUID userId);
}
