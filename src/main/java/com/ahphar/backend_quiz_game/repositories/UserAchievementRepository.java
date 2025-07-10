package com.ahphar.backend_quiz_game.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ahphar.backend_quiz_game.models.User;
import com.ahphar.backend_quiz_game.models.UserAchievement;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long>{
    
    Boolean existsByUserAndAchievement_Code(User user, String code);
    List<UserAchievement> findByUser(User user);
}
