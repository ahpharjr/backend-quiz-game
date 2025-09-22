package com.ahphar.backend_quiz_game.security;

import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.http.ResponseCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ahphar.backend_quiz_game.models.Role;
import com.ahphar.backend_quiz_game.models.User; 
import com.ahphar.backend_quiz_game.models.UserProfile;
import com.ahphar.backend_quiz_game.repositories.UserRepository;
import com.ahphar.backend_quiz_game.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");
        System.out.println("email and name in oauth custom success>>"+ email + " "+ name);

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(generateUniqueUsername(name));
            newUser.setPassword(null);
            newUser.setRegisteredTime(LocalDateTime.now());
            newUser.setVerified(true);
            newUser.setRole(Role.USER);

            // Create profile right away
            UserProfile profile = new UserProfile();
            profile.setLevel(1);
            profile.setUserXp(0);
            profile.setTimeSpent(0L);
            profile.setQuizSet(0);
            profile.setHighestScore(0);
            profile.setProfilePicture("profile1.png");
            profile.setCurrentPhase(1);

            profile.setUser(newUser); // owning side
            newUser.setProfile(profile); // inverse side

            User savedUser = userRepository.save(newUser); // cascades profile save

            return savedUser;
        });

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        System.out.println("accessToken send to fronted::" + accessToken);

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/auth/refresh")
                .maxAge(30 * 24 * 60 * 60)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        ResponseCookie accessCookie = ResponseCookie.from("access_token", accessToken)
                .httpOnly(false) // must be readable by frontend
                .secure(false)
                .path("/")
                .maxAge(5 * 60) // short-lived
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.sendRedirect("http://localhost:5173/oauth2/callback");

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
