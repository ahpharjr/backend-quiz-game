package com.ahphar.backend_quiz_game.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ahphar.backend_quiz_game.DTO.AchievementResponseDTO;
import com.ahphar.backend_quiz_game.DTO.AchievementStatusDTO;
import com.ahphar.backend_quiz_game.models.User;
import com.ahphar.backend_quiz_game.models.UserAchievement;
import com.ahphar.backend_quiz_game.services.AchievementService;
import com.ahphar.backend_quiz_game.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@Tag(name = "Achievements", description = "Operations related to achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;
    private final UserService userService;

    @Operation(
        summary = "Get all achievements",
        description = "Returns a list of all possible achievements in the quiz game.",
        security = @SecurityRequirement(name = "BearerAuth")
    )
    @GetMapping("/achievements")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<AchievementResponseDTO>> getAllAchievements(){
        
        List<AchievementResponseDTO> responseDTOs = achievementService.getAllAchievements();

        return ResponseEntity.status(HttpStatus.OK).body(responseDTOs);
    }

    @Operation(
        summary = "Get achievements of current user",
        description = "Returns a list of achievements unlocked by the currently authenticated user.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/users/me/achievements")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserAchievement>> getUserAchievements(Authentication authentication){
        User user = userService.getCurrentUser(authentication);
        List<UserAchievement> achievements = achievementService.getUserAchievements(user);
        
        return ResponseEntity.status(HttpStatus.OK).body(achievements);
    }

    @Operation(
        summary = "Get achievement status of current user",
        description = "Returns the progress or status of all achievements for the authenticated user, including locked/unlocked state.",
        security = @SecurityRequirement(name = "BearerAuth")
    )
    @GetMapping("/users/me/achievement-status")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<AchievementStatusDTO>> getAchievementStatus(Authentication authentication) {
        User user = userService.getCurrentUser(authentication);

        List<AchievementStatusDTO> statusDTOs = achievementService.getAchievementStatusDTOs(user);

        return ResponseEntity.status(HttpStatus.OK).body(statusDTOs);
    }

}
