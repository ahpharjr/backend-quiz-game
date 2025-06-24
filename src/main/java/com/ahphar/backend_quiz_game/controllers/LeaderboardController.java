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

@RestController
@RequestMapping("/leaderboard")
public class LeaderboardController {
    
    private final LeaderboardService leaderboardService;
    private final UserService userService;
    private final PhaseService phaseService;
    private final QuizService quizService;

    public LeaderboardController(LeaderboardService leaderboardService, UserService userService, PhaseService phaseService, QuizService quizService) {
        this.leaderboardService = leaderboardService;
        this.userService = userService;
        this.phaseService = phaseService;
        this.quizService = quizService;
    }

    @PostMapping("/phases/{phaseId}/add-leaderboard")
    public ResponseEntity<String> addPhaseLeaderboard(@PathVariable Long phaseId, Authentication auth){
        User user = userService.getCurrentUser(auth);
        Phase phase = phaseService.getPhaseById(phaseId);

        leaderboardService.createPhaseLeaderboardIfAbsent(user, phase);
        return ResponseEntity.ok("Phase leaderboard entry created");
    }

    @GetMapping("/phases/{phaseId}")
    public ResponseEntity<List<PhaseLeaderboardResponseDTO>> getTopPhaseLeaderboard(@PathVariable Long phaseId) {
        List<PhaseLeaderboardResponseDTO> topPhaseLeaderboard = leaderboardService.getTopPhaseLeaderboard(phaseId);

        return ResponseEntity.ok(topPhaseLeaderboard);
    }

    @PostMapping("/quizzes/{quizId}/add-leaderboard")
    public ResponseEntity<String> addQuizLeaderboard(@PathVariable Long quizId, Authentication auth){
        User user = userService.getCurrentUser(auth);
        Quiz quiz = quizService.getQuizById(quizId);

        leaderboardService.createQuizLeaderboardIfAbsent(user, quiz);
        return ResponseEntity.ok("Quiz leaderboard entry created");
    }

    @GetMapping("/quizzes/{quizId}")
    public ResponseEntity<List<QuizLeaderboardResponseDTO>> getTopQuizLeaderboard(@PathVariable Long quizId) {
        List<QuizLeaderboardResponseDTO> topQuizLeaderboard = leaderboardService.getTopQuizLeaderboard(quizId);
        return ResponseEntity.ok(topQuizLeaderboard);
    }

}
