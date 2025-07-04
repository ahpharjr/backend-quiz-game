package com.ahphar.backend_quiz_game.controllers;

import com.ahphar.backend_quiz_game.DTO.UpdateProfilePictureRequestDTO;
import com.ahphar.backend_quiz_game.DTO.UpdateUsernameRequestDTO;
import com.ahphar.backend_quiz_game.DTO.UserResponseDTO;
import com.ahphar.backend_quiz_game.mapper.UserMapper;
import com.ahphar.backend_quiz_game.models.User;
import com.ahphar.backend_quiz_game.services.UserService;
import com.ahphar.backend_quiz_game.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, UserMapper userMapper, JwtUtil jwtUtil) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/user-info")
    public ResponseEntity<UserResponseDTO> getCurrentUser(Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        UserResponseDTO userResponseDTO = userMapper.toDto(user);

        return ResponseEntity.ok(userResponseDTO);
    }

    @PutMapping("/update-username")
    public ResponseEntity<?> updateUsername(
            Authentication authentication,
            @RequestBody UpdateUsernameRequestDTO requestDTO) {

        String currentUsername = authentication.getName();

        try {
            User updatedUser = userService.updateUsername(currentUsername, requestDTO.getNewUsername());
            String newToken = jwtUtil.generateToken(updatedUser.getUsername());
            UserResponseDTO responseDTO = userMapper.toDto(updatedUser);

            return ResponseEntity.ok(Map.of(
                    "token", newToken,
                    "user", responseDTO
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/update-profile-picture")
    public ResponseEntity<?> updateProfilePicture(Authentication auth,@Validated @RequestBody UpdateProfilePictureRequestDTO requestDTO){
        User user = userService.getCurrentUser(auth);
        userService.updateProfilePicture(user, requestDTO.getProfilePicture());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Profile picture updated successfully.");
            response.put("profilePicture", requestDTO.getProfilePicture());

            return ResponseEntity.ok(response);
    }


}
