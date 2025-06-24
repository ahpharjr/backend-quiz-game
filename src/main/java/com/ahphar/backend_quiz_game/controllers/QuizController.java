package com.ahphar.backend_quiz_game.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import com.ahphar.backend_quiz_game.models.Answer;
import com.ahphar.backend_quiz_game.models.Question;
import com.ahphar.backend_quiz_game.services.QuizService;

import java.util.List;

@RestController
@RequestMapping("/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;
    
    @GetMapping("/{quizId}/questions")
    public ResponseEntity<List<Question>> getAllQuestions(@PathVariable Long quizId){
        List<Question> questions = quizService.getQuestionsByQuizSet(quizId);

        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{quizId}/answers")
    public ResponseEntity<List<Answer>> getAllAnswersByQuiz(@PathVariable Long quizId){
        List<Answer> answers = quizService.getAnswersByQuiz(quizId);

        return ResponseEntity.ok(answers);
    }
}
