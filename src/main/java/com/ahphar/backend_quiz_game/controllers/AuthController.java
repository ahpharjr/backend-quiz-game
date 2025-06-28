package com.ahphar.backend_quiz_game.controllers;

import com.ahphar.backend_quiz_game.DTO.LoginResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ahphar.backend_quiz_game.DTO.LoginRequestDTO;
import com.ahphar.backend_quiz_game.DTO.RegisterRequestDTO;
import com.ahphar.backend_quiz_game.exception.EmailAlreadyExistsException;
import com.ahphar.backend_quiz_game.exception.NameAlreadyExistsException;
import com.ahphar.backend_quiz_game.services.AuthService;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register (@Validated @RequestBody RegisterRequestDTO requestDTO){
        try {
            authService.register(requestDTO);
            return ResponseEntity.ok("Register successfully");
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.badRequest().body("Email already exists");
        } catch (NameAlreadyExistsException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Validated @RequestBody LoginRequestDTO loginRequestDTO){

        Optional<String> tokenOptional = authService.authenticate(loginRequestDTO);
        if(tokenOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = tokenOptional.get();
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

}
