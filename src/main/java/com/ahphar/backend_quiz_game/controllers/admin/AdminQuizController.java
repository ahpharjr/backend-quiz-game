package com.ahphar.backend_quiz_game.controllers.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ahphar.backend_quiz_game.admin.DTOs.QuizRespDTO;
import com.ahphar.backend_quiz_game.admin.services.AdQuizService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminQuizController {

    private final AdQuizService quizService;
    
    @GetMapping("/quizzes/{quizId}")
    public ResponseEntity<QuizRespDTO> getQuiz(@PathVariable Long quizId){
        QuizRespDTO quiz = quizService.getQuizById(quizId);

        return ResponseEntity.ok(quiz);
    }
}
