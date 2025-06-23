package com.ahphar.backend_quiz_game.services;

import com.ahphar.backend_quiz_game.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ahphar.backend_quiz_game.DTO.LoginRequestDTO;
import com.ahphar.backend_quiz_game.DTO.RegisterRequestDTO;
import com.ahphar.backend_quiz_game.exception.EmailAlreadyExistsException;
import com.ahphar.backend_quiz_game.exception.NameAlreadyExistsException;

import java.util.Optional;

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

    public Optional<String> authenticate(LoginRequestDTO loginRequestDTO) {
        Optional<String> token = userService
                .findByUsername(loginRequestDTO.getUsername())
                .filter(u -> passwordEncoder.matches(loginRequestDTO.getPassword() ,
                        u.getPassword()))
                .map(u-> jwtUtil.generateToken(u.getUsername()));

        return token;
    }

//    public boolean login(LoginRequestDTO loginRequestDTO){
//        return playerRepository.findByName(loginRequestDTO.getName())
//        .map(user -> passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword()))
//        .orElse(false);
//    }
}