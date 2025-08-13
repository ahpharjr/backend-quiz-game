package com.ahphar.backend_quiz_game.controllers.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ahphar.backend_quiz_game.DTO.MessageResponse;
import com.ahphar.backend_quiz_game.DTO.QuestionRequestDTO;
import com.ahphar.backend_quiz_game.DTO.QuestionResponseDTO;
import com.ahphar.backend_quiz_game.services.QuestionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin Question Management", description = "APIs for managing questions in the quiz game")
@RequiredArgsConstructor
public class AdminQuestionController {

    private final QuestionService questionService;
    
    @Operation(
        summary = "Get all questions from a quiz",
        description = "Retrieves a list of all questions associated with a specific quiz.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/quizzes/{quizId}/questions")
    public ResponseEntity<List<QuestionResponseDTO>> getQuestionsByQuizId(@PathVariable Long quizId) {
        List<QuestionResponseDTO> questions = questionService.getQuestionsByQuizId(quizId);

        return ResponseEntity.ok(questions);
    }

    @Operation(
        summary = "Create new question",
        description = "Create new question for a quiz",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/quizzes/{quizId}/question")
    public ResponseEntity<MessageResponse> createQuestion(@PathVariable Long quizId,@Valid @RequestBody QuestionRequestDTO requestDto){
        
        questionService.createQuestion(requestDto, quizId);
        return ResponseEntity.status(HttpStatus.CREATED)
                         .body(new MessageResponse("Question created successfully"));
    }

    @Operation(
        summary = "Update question",
        description = "Update an existing question by its ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/questions/{questionId}")
    public ResponseEntity<MessageResponse> updateQuestion(@PathVariable Long questionId, @Valid @RequestBody QuestionRequestDTO requestDto){
        questionService.updateQuestion(questionId, requestDto);
        return ResponseEntity.ok(new MessageResponse("Question updated successfully"));
    }

    @Operation(
        summary = "Delete question",
        description = "Delete a question by its ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<MessageResponse> deleteQuestion(@PathVariable Long questionId) {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.ok(new MessageResponse("Question deleted successfully"));
    }
}
