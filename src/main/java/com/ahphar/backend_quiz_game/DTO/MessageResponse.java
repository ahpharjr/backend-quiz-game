package com.ahphar.backend_quiz_game.DTO;

import lombok.Data;

@Data
public class MessageResponse {
    
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
