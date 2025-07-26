package com.ahphar.backend_quiz_game.security;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ahphar.backend_quiz_game.models.User;
import com.ahphar.backend_quiz_game.repositories.UserRepository;
import com.ahphar.backend_quiz_game.services.UserProfileService;
import com.ahphar.backend_quiz_game.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler{
    
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserProfileService userProfileService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException{

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

            // Find or create user
            userRepository.findByEmail(email).orElseGet(() -> {
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setUsername(generateUniqueUsername(name));
                newUser.setPassword(null); // password not needed for OAuth users
                newUser.setRegisteredTime(LocalDateTime.now());

                User savedUser = userRepository.save(newUser);
                userProfileService.createDefaultProfile(savedUser);

                return savedUser;
            });

        // Generate JWT
        String jwtToken = jwtUtil.generateToken(oauthUser.getAttribute("name"));
        
        // Redirect or return token in response (based on your frontend)
        response.setContentType("application/json");
        response.getWriter().write("{\"token\":\"" + jwtToken + "\"}");
        String redirectUri = "your.app://oauth2redirect?token=" + jwtToken;

        response.sendRedirect(redirectUri);

    }

    private String generateUniqueUsername(String baseName) {
        String base = baseName.toLowerCase().replaceAll("\\s+", "");
        String username = base;
        int counter = 1;
        while (userRepository.existsByUsername(username)) {
            username = base + counter;
            counter++;
        }
        return username;
    }

}
