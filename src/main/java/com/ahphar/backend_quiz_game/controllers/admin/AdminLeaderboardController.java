package com.ahphar.backend_quiz_game.controllers.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ahphar.backend_quiz_game.DTO.PhaseLbListRespDTO;
import com.ahphar.backend_quiz_game.DTO.QuizLbListRespDTO;
import com.ahphar.backend_quiz_game.admin.services.AdLeaderboardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminLeaderboardController {

    private final AdLeaderboardService adLeaderboardService;

    @RequestMapping("/phaseLeaderboards")
    public ResponseEntity<List<PhaseLbListRespDTO>> getPhaseLeaderboards() {
        List<PhaseLbListRespDTO> phaseLeaderboards = adLeaderboardService.getAllPhaseLeaderboards();

        return ResponseEntity.ok(phaseLeaderboards);
    }

    @GetMapping("/quizLeaderboards")
    public ResponseEntity<List<QuizLbListRespDTO>> getQuizLeaderboards() {
        List<QuizLbListRespDTO> quizLeaderboard = adLeaderboardService.getAllQuizLeaderboards();
        return ResponseEntity.ok(quizLeaderboard);
    }
}
