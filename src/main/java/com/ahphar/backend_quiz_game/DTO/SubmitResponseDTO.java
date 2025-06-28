package com.ahphar.backend_quiz_game.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubmitResponseDTO {
    
    private String message;
    private boolean newRecord;
    private UserProfileDTO updatedProfile;
}

