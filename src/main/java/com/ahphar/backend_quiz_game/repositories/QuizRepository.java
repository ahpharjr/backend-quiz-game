package com.ahphar.backend_quiz_game.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ahphar.backend_quiz_game.models.Quiz;
import com.ahphar.backend_quiz_game.models.Topic;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByTopic_Phase_PhaseId(long phaseId);
    List<Quiz> findByTopic_Phase_PhaseId(int phaseId);
    Quiz findByTopic(Topic topic);
}
