package com.ahphar.backend_quiz_game.services;

import com.ahphar.backend_quiz_game.DTO.RegisterRequestDTO;
import com.ahphar.backend_quiz_game.mapper.UserMapper;
import com.ahphar.backend_quiz_game.models.User;
import com.ahphar.backend_quiz_game.repositories.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserProfileService userProfileService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(UserRepository playerRepository, UserMapper playerMapper, 
                        UserProfileService userProfileService, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = playerRepository;
        this.userMapper = playerMapper;
        this.userProfileService = userProfileService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
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

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }


    // public void save(RegisterRequestDTO requestDTO) {

    //     User user = userMapper.toModel(requestDTO);
    //     String code = generateVerificationCode();
    //     user.setVerificationCode(code);
    //     user.setCodeExpiryTime(LocalDateTime.now().plusMinutes(15));
    //     user.setVerified(false);

    //     User savedUser = userRepository.save(user);

    //     userProfileService.createDefaultProfile(savedUser);

    //     emailService.sendVerificationCodeEmail(user.getEmail(), code);

    // }
    public void save(RegisterRequestDTO requestDTO) {
        User user = userMapper.toModel(requestDTO);
        User savedUser = userRepository.save(user);

        userProfileService.createDefaultProfile(savedUser);

        generateAndSendVerificationCode(savedUser);
    }

    public void generateAndSendVerificationCode(User user) {
        String code = generateVerificationCode();
        user.setVerificationCode(code);
        user.setCodeExpiryTime(LocalDateTime.now().plusMinutes(15));
        user.setVerified(false);

        userRepository.save(user);
        emailService.sendVerificationCodeEmail(user.getEmail(), code);
    }


    public User createNewUserIfNotExists(String email, String username) {

        if(!existsByEmail(email) ) {

            if (existsByUsername(username)) {
                username = generateUniqueUsername(username);
            }

            User user = new User();
            user.setEmail(email);
            user.setUsername(username);
            // user.setPassword(null);
            user.setPassword(passwordEncoder.encode("password@123")+ "12");
            user.setRegisteredTime(LocalDateTime.now());
            user.setVerified(true);

            // Save user first to generate ID
            user = userRepository.save(user); 
            
            // Then create profile
            userProfileService.createDefaultProfile(user);
            
            return user;
        }
        return userRepository.findByEmail(email).orElseThrow();
    }

    public String generateUniqueUsername(String baseUsername) {
        String username = baseUsername;
        int suffix = 1;

        while (userRepository.existsByUsername(username)) {
            username = baseUsername + suffix;
            suffix++;
        }

        return username;
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

    public void updateProfilePicture(User currentUser, String profilePicture ){
        currentUser.getProfile().setProfilePicture(profilePicture);
        userRepository.save(currentUser);
    }

    public User getCurrentUser(Authentication auth) {
    String username = auth.getName();
    return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public void completeEmailVerification(User user) {
        user.setVerified(true);
        user.setVerificationCode(null);
        user.setCodeExpiryTime(null);
        userRepository.save(user);
    }

}
