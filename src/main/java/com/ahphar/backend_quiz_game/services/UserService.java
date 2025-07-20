package com.ahphar.backend_quiz_game.services;

import com.ahphar.backend_quiz_game.DTO.RegisterRequestDTO;
import com.ahphar.backend_quiz_game.exception.UserNotFoundException;
import com.ahphar.backend_quiz_game.mapper.UserMapper;
import com.ahphar.backend_quiz_game.models.Role;
import com.ahphar.backend_quiz_game.models.User;
import com.ahphar.backend_quiz_game.repositories.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void deleteUser(User user){
        userRepository.delete(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    public void save(RegisterRequestDTO requestDTO) {
        User user = userMapper.toModel(requestDTO);
        user.setRole(Role.USER);
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

    public void sendResetPasswordCode(User user){
        String code = generateVerificationCode();
        user.setVerificationCode(code);
        user.setCodeExpiryTime(LocalDateTime.now().plusMinutes(10));
        
        userRepository.save(user);
        emailService.sendResetPasswordCode(user.getEmail(), code);
    }

    public void resetPassword(User user, String newPassword){

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setVerificationCode(null);
        user.setCodeExpiryTime(null);
        userRepository.save(user);
    }


    public User createNewUserIfNotExists(String email, String username) {

        if(!existsByEmail(email) ) {

            if (existsByUsername(username)) {
                username = generateUniqueUsername(username);
            }

            User user = new User();
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode("password@123")+ "12");
            user.setRegisteredTime(LocalDateTime.now());
            user.setRole(Role.USER);
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


    public User updateUsername(User user, String newUsername) {
        if(userRepository.existsByUsername(newUsername)) {
            throw new IllegalArgumentException("Username already taken.");
        }

        user.setUsername(newUsername);
        return userRepository.save(user);
    }

    public void updateProfilePicture(User currentUser, String profilePicture ){
        currentUser.getProfile().setProfilePicture(profilePicture);
        userRepository.save(currentUser);
    }

    public User getCurrentUser(Authentication auth) {
    String email = auth.getName();
    return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found: " + email));
    }

    public void completeEmailVerification(User user) {
        user.setVerified(true);
        user.setVerificationCode(null);
        user.setCodeExpiryTime(null);
        userRepository.save(user);
    }

}
