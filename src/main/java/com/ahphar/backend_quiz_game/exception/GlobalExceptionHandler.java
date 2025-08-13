package com.ahphar.backend_quiz_game.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }
    
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex){
        
        log.warn("Email already exists: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Email already exists");

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NameAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleNameAlreadyExistsException(NameAlreadyExistsException ex){

        log.warn("Player name already taken: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Player name already taken!");

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException ex){

        log.warn("User with this email doesn't exist: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        errors.put("message", "User with this email doesn't exist!");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(PhaseNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePhaseNotFoundException(PhaseNotFoundException ex){

        log.warn("Phase not found: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Phase not found!");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(TopicNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleTopicNotFoundException(TopicNotFoundException ex){

        log.warn("Topic not found: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Topic not found!");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(QuizNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleQuizNotFoundException(QuizNotFoundException ex){

        log.warn("Quiz not found: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Quiz not found!");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleQuestionNotFoundException(QuestionNotFoundException ex){

        log.warn("Question not found: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Question not found!");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

}
