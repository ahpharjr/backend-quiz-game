package com.ahphar.backend_quiz_game.controllers.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ahphar.backend_quiz_game.admin.DTOs.UserRespDTO;
import com.ahphar.backend_quiz_game.admin.services.AdUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdUserService userService;
    
    @GetMapping
    public ResponseEntity<List<UserRespDTO>> getAllUsers(){
        List<com.ahphar.backend_quiz_game.admin.DTOs.UserRespDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);

    }
}
