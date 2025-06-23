package com.ahphar.backend_quiz_game.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ahphar.backend_quiz_game.models.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long>{

    List<Answer> findByQuestion_QuestionId(Long questionId);
    List<Answer> findByQuestion_Quiz_QuizId(Long quizId);

}
