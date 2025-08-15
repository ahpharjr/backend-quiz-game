package com.ahphar.backend_quiz_game.exception;

public class AnswerNotFoundException extends RuntimeException{
    public AnswerNotFoundException(String message){
        super(message);
    }
}
