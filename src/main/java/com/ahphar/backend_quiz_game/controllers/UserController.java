package com.ahphar.backend_quiz_game.controllers;

import com.ahphar.backend_quiz_game.DTO.UpdateProfilePictureRequestDTO;
import com.ahphar.backend_quiz_game.DTO.UpdateUsernameRequestDTO;
import com.ahphar.backend_quiz_game.DTO.UserResponseDTO;
import com.ahphar.backend_quiz_game.mapper.UserMapper;
import com.ahphar.backend_quiz_game.models.User;
import com.ahphar.backend_quiz_game.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Tag(name = "User" , description = "Operations related to user profile management")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(
        summary = "Get current logged-in user info",
        description = "Retrieves the profile information of the currently authenticated user.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(Authentication authentication) {

        User user = userService.getCurrentUser(authentication);
        UserResponseDTO userResponseDTO = userMapper.toDto(user);

        return ResponseEntity.ok(userResponseDTO);
    }

    @Operation(
        summary = "Update username",
        description = "Updates the username of the currently authenticated user and returns a new JWT token.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/me/username")
    public ResponseEntity<?> updateUsername(
            Authentication authentication,
            @RequestBody UpdateUsernameRequestDTO requestDTO) {

        User user = userService.getCurrentUser(authentication);

        try {
            User updatedUser = userService.updateUsername(user, requestDTO.getNewUsername());
            UserResponseDTO responseDTO = userMapper.toDto(updatedUser);

            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(
        summary = "Update profile picture",
        description = "Updates the profile picture of the currently authenticated user.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/me/profile-picture")
    public ResponseEntity<?> updateProfilePicture(Authentication auth,@Validated @RequestBody UpdateProfilePictureRequestDTO requestDTO){
        User user = userService.getCurrentUser(auth);
        userService.updateProfilePicture(user, requestDTO.getProfilePicture());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Profile picture updated successfully.");
            response.put("profilePicture", requestDTO.getProfilePicture());

            return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Delete user account",
        description = "Deletes the currently authenticated user's account.",
        security = @SecurityRequirement(name = "bearerAuth")
        )
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteUser(Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        userService.deleteUser(user);

        return ResponseEntity.ok(Map.of("message", "User deleted successfully."));
    }

}
