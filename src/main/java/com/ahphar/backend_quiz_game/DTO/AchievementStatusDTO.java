package com.ahphar.backend_quiz_game.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AchievementStatusDTO {
    private String code;
    private String title;
    private String imageUrl;
    private String description;
    private boolean unlocked;
}

