package com.ahphar.backend_quiz_game.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CodeRequestDTO {
    @NotBlank(message = "Email is required")
    @Email(message = "This must be an email")
    private String email;
}
