package com.ahphar.backend_quiz_game.exception;

public class UserNotFoundException extends RuntimeException
{
    public UserNotFoundException(String message){
        super(message);
    }
}
