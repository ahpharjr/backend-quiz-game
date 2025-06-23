package com.ahphar.backend_quiz_game.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ahphar.backend_quiz_game.models.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>{
    List<Question> findByQuiz_QuizId(Long quizId);
}
