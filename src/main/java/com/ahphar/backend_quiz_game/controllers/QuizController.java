package com.ahphar.backend_quiz_game.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ahphar.backend_quiz_game.DTO.AnswerResponseDTO;
import com.ahphar.backend_quiz_game.DTO.QuestionResponseDTO;
import com.ahphar.backend_quiz_game.DTO.SubmitRequestDTO;
import com.ahphar.backend_quiz_game.DTO.SubmitResponseDTO;
import com.ahphar.backend_quiz_game.models.User;
import com.ahphar.backend_quiz_game.services.AchievementService;
import com.ahphar.backend_quiz_game.services.AnswerService;
import com.ahphar.backend_quiz_game.services.QuestionService;
import com.ahphar.backend_quiz_game.services.QuizService;
import com.ahphar.backend_quiz_game.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List; 

@RestController
@RequestMapping("/quizzes")
@Tag(name = "Quizzes", description = "Quiz operations such as retrieving questions and submitting answers")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;
    private final UserService userService;
    private final AchievementService achievementService;
    private final QuestionService questionService;
    private final AnswerService answerService;

    
    @Operation(
        summary = "Get questions for a quiz",
        description = "Returns all questions belonging to a specific quiz by quiz ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{quizId}/questions")
    public ResponseEntity<List<QuestionResponseDTO>> getAllQuestions(@PathVariable Long quizId){
        List<QuestionResponseDTO> questions = questionService.getQuestionsByQuizId(quizId);

        return ResponseEntity.ok(questions);
    }

        @Operation(
        summary = "Get all answers for a quiz",
        description = "Returns all answers belonging to a specific quiz by quiz ID.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{quizId}/answers")
    public ResponseEntity<List<AnswerResponseDTO>> getAllAnswersByQuiz(@PathVariable Long quizId){
        List<AnswerResponseDTO> answers = answerService.getAllAnswersByQuizId(quizId);

        return ResponseEntity.ok(answers);
    }

    @Operation(
        summary = "Submit a quiz attempt",
        description = "Submits score details for the specified quiz and returns user score details and updated profiles. Also checks for achievements after submission.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/{quizId}/submit")
    public ResponseEntity<SubmitResponseDTO> submitQuiz(
            @PathVariable Long quizId,
            @Validated @RequestBody SubmitRequestDTO requestDTO,
            Authentication authentication) {

        User currentUser = userService.getCurrentUser(authentication);
        SubmitResponseDTO response = quizService.submitQuiz(currentUser, quizId, requestDTO);
        achievementService.evaluateAchievements(currentUser);
        return ResponseEntity.ok(response);
    }

}
