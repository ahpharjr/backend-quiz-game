package com.ahphar.backend_quiz_game.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ahphar.backend_quiz_game.DTO.AchievementResponseDTO;
import com.ahphar.backend_quiz_game.DTO.AchievementStatusDTO;
import com.ahphar.backend_quiz_game.models.User;
import com.ahphar.backend_quiz_game.models.UserAchievement;
import com.ahphar.backend_quiz_game.services.AchievementService;
import com.ahphar.backend_quiz_game.services.UserService;

@RestController
@RequestMapping("/achievements")
public class AchievementController {

    private final AchievementService achievementService;
    private final UserService userService;

    public AchievementController(AchievementService achievementService, UserService userService){
        this.achievementService = achievementService;
        this.userService = userService;
    }
    
    @GetMapping()
    public ResponseEntity<List<AchievementResponseDTO>> getAllAchievements(){
        
        List<AchievementResponseDTO> responseDTOs = achievementService.getAllAchievements();

        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/user-achievements")
    public ResponseEntity<List<UserAchievement>> getUserAchievements(Authentication authentication){
        User user = userService.getCurrentUser(authentication);
        List<UserAchievement> achievements = achievementService.getUserAchievements(user);
        
        return ResponseEntity.ok(achievements);
    }

    @GetMapping("/user-achievements-status")
    public ResponseEntity<List<AchievementStatusDTO>> getAchievementStatus(Authentication authentication) {
        User user = userService.getCurrentUser(authentication);

        List<AchievementStatusDTO> statusDTOs = achievementService.getAchievementStatusDTOs(user);

        return ResponseEntity.ok(statusDTOs);
    }

}
