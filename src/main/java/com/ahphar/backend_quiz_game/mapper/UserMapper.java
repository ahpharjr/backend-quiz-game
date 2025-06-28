package com.ahphar.backend_quiz_game.mapper;

import java.time.LocalDateTime;

import com.ahphar.backend_quiz_game.DTO.UserResponseDTO;
import com.ahphar.backend_quiz_game.models.UserProfile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ahphar.backend_quiz_game.DTO.RegisterRequestDTO;
import com.ahphar.backend_quiz_game.models.User;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;
    public UserMapper(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    public User toModel(RegisterRequestDTO registerDTO){
        User user = new User();

        user.setEmail(registerDTO.getEmail());
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRegisteredTime(LocalDateTime.now());

        return user;
    }

    public UserResponseDTO toDto(User user){
        UserResponseDTO dto = new UserResponseDTO();

        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());

        UserProfile profile = user.getProfile();

        if(profile != null){
            dto.setLevel(profile.getLevel());
            dto.setUserXp(profile.getUserXp());
            dto.setTimeSpent(profile.getTimeSpent());
            dto.setQuizSet(profile.getQuizSet());
            dto.setHighestScore(profile.getHighestScore());
            dto.setProfilePicture(profile.getProfilePicture());
            dto.setCurrentPhase(profile.getCurrentPhase());
        }

        return dto;
    }
}