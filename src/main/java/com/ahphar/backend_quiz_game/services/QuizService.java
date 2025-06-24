package com.ahphar.backend_quiz_game.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahphar.backend_quiz_game.models.Answer;
import com.ahphar.backend_quiz_game.models.Question;
import com.ahphar.backend_quiz_game.models.Quiz;
import com.ahphar.backend_quiz_game.repositories.AnswerRepository;
import com.ahphar.backend_quiz_game.repositories.QuestionRepository;
import com.ahphar.backend_quiz_game.repositories.QuizRepository;

@Service
public class QuizService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuizRepository quizRepository;

    public QuizService(
        QuestionRepository questionRepository,
        AnswerRepository answerRepository,
        QuizRepository quizRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.quizRepository = quizRepository;
    }
    
    public List<Question> getQuestionsByQuizSet(Long quizId){
        return questionRepository.findByQuiz_QuizId(quizId);
    }

    public List<Answer> getAnswersByQuiz(Long quizId){
        return answerRepository.findByQuestion_Quiz_QuizId(quizId);
    }

    public Quiz getQuizById(Long quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));
    }
}