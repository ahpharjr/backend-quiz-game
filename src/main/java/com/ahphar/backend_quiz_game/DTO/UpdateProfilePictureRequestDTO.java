package com.ahphar.backend_quiz_game.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfilePictureRequestDTO {

    @NotBlank(message = "Profile picture is required")
    private String profilePicture;
}
