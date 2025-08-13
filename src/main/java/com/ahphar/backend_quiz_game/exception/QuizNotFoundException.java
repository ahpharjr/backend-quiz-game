package com.ahphar.backend_quiz_game.exception;

public class QuizNotFoundException extends RuntimeException{
    
    public QuizNotFoundException(String message){
        super(message);
    }
}
