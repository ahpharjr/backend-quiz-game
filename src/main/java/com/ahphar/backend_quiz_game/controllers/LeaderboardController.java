package com.ahphar.backend_quiz_game.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ahphar.backend_quiz_game.DTO.PhaseLeaderboardResponseDTO;
import com.ahphar.backend_quiz_game.DTO.QuizLeaderboardResponseDTO;
import com.ahphar.backend_quiz_game.models.Phase;
import com.ahphar.backend_quiz_game.models.Quiz;
import com.ahphar.backend_quiz_game.models.User;
import com.ahphar.backend_quiz_game.services.LeaderboardService;
import com.ahphar.backend_quiz_game.services.PhaseService;
import com.ahphar.backend_quiz_game.services.QuizService;
import com.ahphar.backend_quiz_game.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/leaderboard")
@Tag(name = "Leaderboard", description = "APIs for quiz and phase leaderboards")
@RequiredArgsConstructor
public class LeaderboardController {
    
    private final LeaderboardService leaderboardService;
    private final UserService userService;
    private final PhaseService phaseService;
    private final QuizService quizService;


    @Operation(
        summary = "Add user to phase leaderboard",
        description = "Creates a phase leaderboard entry for the authenticated user if one does not already exist.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/phases/{phaseId}")
    public ResponseEntity<String> addPhaseLeaderboard(@PathVariable Long phaseId, Authentication auth){
        User user = userService.getCurrentUser(auth);
        Phase phase = phaseService.getPhaseById(phaseId);

        leaderboardService.createPhaseLeaderboardIfAbsent(user, phase);
        return ResponseEntity.ok("Phase leaderboard entry created");
    }

    @Operation(
        summary = "Get top users in a phase leaderboard",
        description = "Returns a list of top users for the given phase, sorted by their scores and time.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/phases/{phaseId}")
    public ResponseEntity<List<PhaseLeaderboardResponseDTO>> getTopPhaseLeaderboard(@PathVariable Long phaseId) {
        List<PhaseLeaderboardResponseDTO> topPhaseLeaderboard = leaderboardService.getTopPhaseLeaderboard(phaseId);

        return ResponseEntity.ok(topPhaseLeaderboard);
    }

    @Operation(
        summary = "Add user to quiz leaderboard",
        description = "Creates a quiz leaderboard entry for the authenticated user if one does not already exist.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/quizzes/{quizId}")
    public ResponseEntity<String> addQuizLeaderboard(@PathVariable Long quizId, Authentication auth){
        User user = userService.getCurrentUser(auth);
        Quiz quiz = quizService.getQuizById(quizId);

        leaderboardService.createQuizLeaderboardIfAbsent(user, quiz);
        return ResponseEntity.ok("Quiz leaderboard entry created");
    }

    @Operation(
        summary = "Get top users in a quiz leaderboard",
        description = "Returns a list of top users for the given quiz, sorted by their scores and time.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/quizzes/{quizId}")
    public ResponseEntity<List<QuizLeaderboardResponseDTO>> getTopQuizLeaderboard(@PathVariable Long quizId) {
        List<QuizLeaderboardResponseDTO> topQuizLeaderboard = leaderboardService.getTopQuizLeaderboard(quizId);
        return ResponseEntity.ok(topQuizLeaderboard);
    }

}
