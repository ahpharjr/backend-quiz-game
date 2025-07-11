package com.ahphar.backend_quiz_game.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ahphar.backend_quiz_game.models.Achievement;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long>{
    Optional<Achievement> findByCode(String code);
}
