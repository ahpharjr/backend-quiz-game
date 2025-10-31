package com.ahphar.backend_quiz_game.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ahphar.backend_quiz_game.models.Quiz;
import com.ahphar.backend_quiz_game.models.QuizLeaderboard;
import com.ahphar.backend_quiz_game.models.User;

@Repository
public interface QuizLeaderboardRepository extends JpaRepository<QuizLeaderboard, Long> {

    List<QuizLeaderboard> findTop30ByQuizOrderByPointDescTimeTakenAsc(Quiz quiz);
    Optional<QuizLeaderboard> findByQuizAndUser(Quiz quiz, User currentUser);
    Boolean existsByUserAndQuiz(User user, Quiz quiz);

    @Query("SELECT DISTINCT ql FROM QuizLeaderboard ql JOIN FETCH ql.quiz")
    List<QuizLeaderboard> findAllDistinctByQuiz();
}
