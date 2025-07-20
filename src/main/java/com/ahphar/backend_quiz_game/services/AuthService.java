package com.ahphar.backend_quiz_game.services;

import com.ahphar.backend_quiz_game.util.JwtUtil;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ahphar.backend_quiz_game.DTO.LoginRequestDTO;
import com.ahphar.backend_quiz_game.DTO.RegisterRequestDTO;
import com.ahphar.backend_quiz_game.exception.EmailAlreadyExistsException;
import com.ahphar.backend_quiz_game.exception.NameAlreadyExistsException;
import com.ahphar.backend_quiz_game.exception.UserNotFoundException;
import com.ahphar.backend_quiz_game.models.User;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthService(PasswordEncoder passwordEncoder, UserService userService, JwtUtil jwtUtil) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    public void register(RegisterRequestDTO registerRequestDTO) throws EmailAlreadyExistsException, NameAlreadyExistsException {

        if(userService.existsByUsername(registerRequestDTO.getUsername())) {
            throw new NameAlreadyExistsException("Player name already taken");
        }

        if(userService.existsByEmail(registerRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        
        userService.save(registerRequestDTO);

    }

    public String authenticateOrThrow(LoginRequestDTO loginRequestDTO) {
        User user = userService.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        if (!user.isVerified()) {
            throw new IllegalStateException("Email is not verified");
        }

        return jwtUtil.generateToken(user);
    }

}