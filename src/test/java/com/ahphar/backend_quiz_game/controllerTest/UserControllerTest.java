package com.ahphar.backend_quiz_game.controllerTest;

import com.ahphar.backend_quiz_game.models.User;
import com.ahphar.backend_quiz_game.models.UserProfile;
import com.ahphar.backend_quiz_game.repositories.UserProfileRepository;
import com.ahphar.backend_quiz_game.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private User testUser;

    @BeforeAll
    void setUp() {
        // Create and save test user
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setRegisteredTime(LocalDateTime.now());

        testUser = userRepository.save(testUser); // managed user with ID

        // Create UserProfile (with same ID)
        UserProfile profile = new UserProfile();
        profile.setUser(testUser); // required for @MapsId
        profile.setLevel(3);
        profile.setUserXp(2000);
        profile.setQuizSet(2);
        profile.setTimeSpent(5000L);
        profile.setHighestScore(1500);
        profile.setProfilePicture("avatar.png");
        profile.setCurrentQuizSet(2);
        profile.setCurrentPhase(1);

        userProfileRepository.save(profile); // will now work correctly
    }

    @Test
    @WithMockUser(username = "testuser") // Mock authenticated user
    void testGetUserInfo() throws Exception {
        mockMvc.perform(get("/user-info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.level").value(3))
                .andExpect(jsonPath("$.userXp").value(2000))
                .andExpect(jsonPath("$.quizSet").value(2))
                .andExpect(jsonPath("$.highestScore").value(1500))
                .andExpect(jsonPath("$.pfPicture").value("avatar.png"));
    }
}
