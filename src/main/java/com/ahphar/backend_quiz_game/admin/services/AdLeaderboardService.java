package com.ahphar.backend_quiz_game.admin.services;

import org.springframework.stereotype.Service;

import com.ahphar.backend_quiz_game.DTO.PhaseLbListRespDTO;
import com.ahphar.backend_quiz_game.DTO.QuizLbListRespDTO;
import com.ahphar.backend_quiz_game.mapper.LeaderboardMapper;
import com.ahphar.backend_quiz_game.models.PhaseLeaderboard;
import com.ahphar.backend_quiz_game.models.QuizLeaderboard;
import com.ahphar.backend_quiz_game.repositories.PhaseLeaderboardRepository;
import com.ahphar.backend_quiz_game.repositories.QuizLeaderboardRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdLeaderboardService {

    private final PhaseLeaderboardRepository phaseLeaderboardRepository;
    private final LeaderboardMapper leaderboardMapper;
    private final QuizLeaderboardRepository quizLeaderboardRepository;

    public List<PhaseLbListRespDTO> getAllPhaseLeaderboards() {
        // Fetch all phase leaderboards
        List<PhaseLeaderboard> allLeaderboards = phaseLeaderboardRepository.findAll();

        // Extract unique phases (avoid duplicates)
        Map<Long, PhaseLeaderboard> uniquePhaseMap = new LinkedHashMap<>();
        for (PhaseLeaderboard lb : allLeaderboards) {
            Long phaseId = lb.getPhase().getPhaseId();
            if (!uniquePhaseMap.containsKey(phaseId)) {
                uniquePhaseMap.put(phaseId, lb);
            }
        }

        // Convert to DTO list
        return uniquePhaseMap.values()
                .stream()
                .map(leaderboardMapper::phaseLbListToDto)
                .toList();
    }

    public List<QuizLbListRespDTO> getAllQuizLeaderboards() {
        List<QuizLeaderboard> allLeaderboards = quizLeaderboardRepository.findAll();

        // Extract unique quizzes (avoid duplicates)
        Map<Long, QuizLeaderboard> uniqueQuizMap = new LinkedHashMap<>();
        for (QuizLeaderboard lb : allLeaderboards) {
            Long quizId = lb.getQuiz().getQuizId();
            if (!uniqueQuizMap.containsKey(quizId)) {
                uniqueQuizMap.put(quizId, lb);
            }
        }

        return uniqueQuizMap.values()
                .stream()
                .map(leaderboardMapper::quizLbListToDto)
                .toList();
    }

}
