package com.ahphar.backend_quiz_game.DTO;

public class LoginResponseDTO {

    private final String token;

    public LoginResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {return token;}
}
