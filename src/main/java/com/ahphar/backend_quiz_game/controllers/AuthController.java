package com.ahphar.backend_quiz_game.controllers;

import com.ahphar.backend_quiz_game.DTO.LoginResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ahphar.backend_quiz_game.DTO.EmailCodeDTO;
import com.ahphar.backend_quiz_game.DTO.LoginRequestDTO;
import com.ahphar.backend_quiz_game.DTO.RegisterRequestDTO;
import com.ahphar.backend_quiz_game.DTO.ResetPasswordRequestDTO;
import com.ahphar.backend_quiz_game.DTO.CodeRequestDTO;
import com.ahphar.backend_quiz_game.exception.EmailAlreadyExistsException;
import com.ahphar.backend_quiz_game.exception.NameAlreadyExistsException;
import com.ahphar.backend_quiz_game.exception.UserNotFoundException;
import com.ahphar.backend_quiz_game.models.User;
import com.ahphar.backend_quiz_game.services.AuthService;
import com.ahphar.backend_quiz_game.services.CustomUserDetailsService;
import com.ahphar.backend_quiz_game.services.UserService;
import com.ahphar.backend_quiz_game.util.JwtUtil;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "APIs for user registration, login, and password reset.")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    @Value("${google.clientId}")
    private String googleClientId;

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody RegisterRequestDTO requestDTO) {
        try {
            authService.register(requestDTO);
            return ResponseEntity.ok("Register successfully. Please verify your email.");
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.badRequest().body("Email already exists");
        } catch (NameAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Verify email with code")
    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody EmailCodeDTO dto) {
        User user = userService.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + dto.getEmail()));

        if (user.isVerified()) {
            return ResponseEntity.ok("Already verified");
        }

        if (user.getCodeExpiryTime().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Verification code expired.");
        }

        if (!dto.getCode().equals(user.getVerificationCode())) {
            return ResponseEntity.badRequest().body("Invalid code.");
        }

        userService.completeEmailVerification(user);
        return ResponseEntity.ok("Email verified successfully");
    }

    @Operation(summary = "Resend email verification code")
    @PostMapping("/resend-email-verification")
    public ResponseEntity<?> resendVerificationCode(@RequestBody CodeRequestDTO dto) {

        User user = userService.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + dto.getEmail()));

        if (user.isVerified()) {
            return ResponseEntity.ok("Already verified");
        }

        userService.generateAndSendVerificationCode(user);

        return ResponseEntity.ok("Verification code resent successfully.");
    }

    @Operation(summary = "Send password reset code to email")
    @PostMapping("/password-reset/send-code")
    public ResponseEntity<String> sendResetPasswordCode(@Validated @RequestBody CodeRequestDTO dto) {
        User user = userService.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with this email: " + dto.getEmail()));

        userService.sendResetPasswordCode(user);
        return ResponseEntity.ok("Send reset password code successfully.");
    }

    @Operation(summary = "Verify password reset code")
    @PostMapping("/password-reset/verify-code")
    public ResponseEntity<String> verifiedResetPasswordCode(@Validated @RequestBody EmailCodeDTO dto) {
        User user = userService.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with this email: " + dto.getEmail()));

        if (user.getCodeExpiryTime().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Verification code expired.");
        }

        if (!dto.getCode().equals(user.getVerificationCode())) {
            return ResponseEntity.badRequest().body("Invalid code.");
        }

        return ResponseEntity.ok("Verified reset password code successfully.");
    }

    @Operation(summary = "Reset password")
    @PutMapping("/password-reset")
    public ResponseEntity<String> resetPassword(@Validated @RequestBody ResetPasswordRequestDTO dto) {
        User user = userService.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with this email: " + dto.getEmail()));

        userService.resetPassword(user, dto.getNewPassword());
        return ResponseEntity.ok("Update password successfully!");
    }

    @Operation(summary = "Login with email and password")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            String token = authService.authenticateOrThrow(loginRequestDTO);
            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Please verify your email.");
        } catch (BadCredentialsException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        }
    }

    @Operation(summary = "Login with Google ID token")
    @PostMapping("/login/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, String> body) {
        String idToken = body.get("idToken");
        System.out.println("google client id : " + googleClientId);

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .setIssuer("https://accounts.google.com")
                .setAcceptableTimeSkewSeconds(60 * 5)
                .build();

        try {
            GoogleIdToken token = verifier.verify(idToken);
            if (token != null) {
                Payload payload = token.getPayload();
                String email = payload.getEmail();
                String name = (String) payload.get("name");

                Optional<User> optionalUser = userService.findByEmail(email);
                User user = optionalUser.orElseGet(() -> userService.createNewUserIfNotExists(email, name));

                String jwt = jwtUtil.generateToken(user);
                return ResponseEntity.ok(Map.of("token", jwt));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid ID token");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token verification failed: " + e.getMessage());
        }
    }

    @Operation(summary = "Login with email and password")
    @PostMapping("/web-login")
    public ResponseEntity<?> webLogin(@Validated @RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            User user = userService.findByEmail(loginRequestDTO.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
                throw new BadCredentialsException("Invalid password");
            }

            if (!user.isVerified()) {
                throw new IllegalStateException("Email is not verified");
            }

            String accessToken = jwtUtil.generateAccessToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(user);

            // Build HTTP-only secure cookie for refresh token
            ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Strict") // or "Lax" if frontend/backend are on different domains
                    .path("/api/auth/refresh")
                    .maxAge(7 * 24 * 60 * 60) // 7 days
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                    .body(Map.of("accessToken", accessToken));

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Please verify your email.");
        } catch (BadCredentialsException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    String refreshToken = cookie.getValue();
                    String email = jwtUtil.extractEmail(refreshToken);

                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
                    if (jwtUtil.validateToken(refreshToken, userDetails)) {
                        String newAccessToken = jwtUtil.generateAccessToken((User) userDetails);
                        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/api/auth/refresh")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .build();
    }

}
