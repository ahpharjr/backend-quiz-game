package com.ahphar.backend_quiz_game.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ahphar.backend_quiz_game.models.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByTopic_Phase_PhaseId(long phaseId);
}
