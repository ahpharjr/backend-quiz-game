package com.ahphar.backend_quiz_game.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ahphar.backend_quiz_game.DTO.SubmitRequestDTO;
import com.ahphar.backend_quiz_game.DTO.SubmitResponseDTO;
import com.ahphar.backend_quiz_game.models.Answer;
import com.ahphar.backend_quiz_game.models.Question;
import com.ahphar.backend_quiz_game.models.User;
import com.ahphar.backend_quiz_game.services.QuizService;
import com.ahphar.backend_quiz_game.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/quizzes")
public class QuizController {

    private final QuizService quizService;
    private final UserService userService;

    public QuizController(@Autowired QuizService quizService, @Autowired UserService userService) {
        this.quizService = quizService;
        this.userService = userService;
    }
    
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

    @PostMapping("/{quizId}/submit")
    public ResponseEntity<SubmitResponseDTO> submitQuiz(
            @PathVariable Long quizId,
            @Validated @RequestBody SubmitRequestDTO requestDTO,
            Authentication authentication) {

        User currentUser = userService.getCurrentUser(authentication);
        SubmitResponseDTO response = quizService.submitQuiz(currentUser, quizId, requestDTO);
        return ResponseEntity.ok(response);
    }

    }
