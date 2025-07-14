package com.ahphar.backend_quiz_game.exception;

public class PhaseNotFoundException extends RuntimeException{
    public PhaseNotFoundException(String message) {
        super(message);
    }
}
