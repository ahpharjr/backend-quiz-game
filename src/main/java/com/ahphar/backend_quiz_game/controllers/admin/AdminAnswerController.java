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
import com.ahphar.backend_quiz_game.services.AnswerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import com.ahphar.backend_quiz_game.DTO.AnswerRequestDTO;
import com.ahphar.backend_quiz_game.DTO.AnswerResponseDTO;
import com.ahphar.backend_quiz_game.DTO.MessageResponse;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin Answer Management", description = "Endpoints for managing answers in the quiz game")
public class AdminAnswerController {

    private final AnswerService answerService;

    public AdminAnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }
    
    @Operation(
        summary = "Get all answers by quiz ID",
        description = "Retrieve a list of all answers associated with a specific quiz.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/quizzes/{quizId}/answers")
    public ResponseEntity<List<AnswerResponseDTO>> getAllAnswersByQuizId(@PathVariable Long quizId){
        List<AnswerResponseDTO> answers = answerService.getAllAnswersByQuizId(quizId);
        return ResponseEntity.ok(answers);
    }

    @Operation(
        summary = "Get all answers by question ID",
        description = "Retrieve a list of all answers associated with a specific question.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/questions/{questionId}/answers")
    public ResponseEntity<List<AnswerResponseDTO>> getAllAnswersByQuestionId(@PathVariable Long questionId){
        List<AnswerResponseDTO> answers = answerService.getAllAnswersByQuestionId(questionId);
        return ResponseEntity.ok(answers);
    }

    @Operation(
        summary = "Create a new answer",
        description = "Creates a new answer for a specific question. Requires admin privileges.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/questions/{questionId}/answer")
    public ResponseEntity<MessageResponse> createAnswer(@PathVariable Long questionId, @Valid @RequestBody AnswerRequestDTO requestdto){
        answerService.createAnswer(questionId, requestdto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Answer created successfully"));
    }

    @Operation(
        summary = "Update an existing answer",
        description = "Updates an existing answer for a specific question. Requires admin privileges.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/answers/{answerId}")
    public ResponseEntity<MessageResponse> updateAnswer(@PathVariable Long answerId,@Valid @RequestBody AnswerRequestDTO requestDTO){
        answerService.updateAnswer(answerId, requestDTO);

        return ResponseEntity.ok(new MessageResponse("Answer updated successfully"));
    }

    @Operation(
        summary = "Delete an existing answer",
        description = "Deletes an existing answer for a specific question. Requires admin privileges.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/answers/{answerId}")
    public ResponseEntity<MessageResponse> deleteAnswer(@PathVariable Long answerId){
        answerService.deleteAnswer(answerId);
        return ResponseEntity.ok(new MessageResponse("Answer deleted successfully"));
    }
}
