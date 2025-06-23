package com.ahphar.backend_quiz_game.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahphar.backend_quiz_game.models.Answer;
import com.ahphar.backend_quiz_game.models.Question;
import com.ahphar.backend_quiz_game.repositories.AnswerRepository;
import com.ahphar.backend_quiz_game.repositories.QuestionRepository;

@Service
public class QuizService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;
    
    public List<Question> getQuestionsByQuizSet(Long quizId){
        return questionRepository.findByQuiz_QuizId(quizId);
    }

    public List<Answer> getAnswersByQuiz(Long quizId){
        return answerRepository.findByQuestion_Quiz_QuizId(quizId);
    }
}