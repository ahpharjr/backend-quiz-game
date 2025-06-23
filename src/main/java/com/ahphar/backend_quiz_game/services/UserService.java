package com.ahphar.backend_quiz_game.services;

import com.ahphar.backend_quiz_game.DTO.RegisterRequestDTO;
import com.ahphar.backend_quiz_game.DTO.UserResponseDTO;
import com.ahphar.backend_quiz_game.mapper.UserMapper;
import com.ahphar.backend_quiz_game.models.User;
import com.ahphar.backend_quiz_game.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserProfileService userProfileService;

    public UserService(UserRepository playerRepository, UserMapper playerMapper, UserProfileService userProfileService) {
        this.userRepository = playerRepository;
        this.userMapper = playerMapper;
        this.userProfileService = userProfileService;
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void save(RegisterRequestDTO requestDTO) {
        User user = userMapper.toModel(requestDTO);
        User savedUser = userRepository.save(user);

        userProfileService.createDefaultProfile(savedUser);

    }

    public User updateUsername(String currentUsername, String newUsername) {
        if(userRepository.existsByUsername(newUsername)) {
            throw new IllegalArgumentException("Username already taken.");
        }

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found."));

        user.setUsername(newUsername);
        return userRepository.save(user);
    }

}
