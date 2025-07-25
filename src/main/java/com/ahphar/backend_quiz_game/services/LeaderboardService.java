package com.ahphar.backend_quiz_game.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ahphar.backend_quiz_game.DTO.PhaseLeaderboardResponseDTO;
import com.ahphar.backend_quiz_game.DTO.QuizLeaderboardResponseDTO;
import com.ahphar.backend_quiz_game.mapper.LeaderboardMapper;
import com.ahphar.backend_quiz_game.models.Phase;
import com.ahphar.backend_quiz_game.models.PhaseLeaderboard;
import com.ahphar.backend_quiz_game.models.Quiz;
import com.ahphar.backend_quiz_game.models.QuizLeaderboard;
import com.ahphar.backend_quiz_game.models.User;
import com.ahphar.backend_quiz_game.repositories.PhaseLeaderboardRepository;
import com.ahphar.backend_quiz_game.repositories.QuizLeaderboardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeaderboardService {
    
    private final PhaseLeaderboardRepository phaseLeaderboardRepo;
    private final QuizLeaderboardRepository quizLeaderboardRepo;
    private final LeaderboardMapper leaderboardMapper;

    public PhaseLeaderboard createPhaseLeaderboardIfAbsent(User user, Phase phase){
        
        return phaseLeaderboardRepo.findByPhaseAndUser(phase, user)
            .orElseGet(() -> {
                PhaseLeaderboard leaderboard = new PhaseLeaderboard();
                leaderboard.setUser(user);
                leaderboard.setPhase(phase);
                leaderboard.setPoint(0);
                leaderboard.setTimeTaken(0L);
                return phaseLeaderboardRepo.save(leaderboard);
            });
    }

    public List<PhaseLeaderboardResponseDTO> getTopPhaseLeaderboard(Long phaseId) {
        Phase phase = new Phase();
        phase.setPhaseId(phaseId);
        List<PhaseLeaderboard> topLeaderboards = phaseLeaderboardRepo.findTop30ByPhaseOrderByPointDescTimeTakenAsc(phase);
        
        return topLeaderboards.stream()
            .map(leaderboardMapper::toDto)
            .toList();
    }

    public QuizLeaderboard createQuizLeaderboardIfAbsent(User user, Quiz quiz) {
    
    // Assuming QuizLeaderboard has similar structure to PhaseLeaderboard
    return quizLeaderboardRepo.findByQuizAndUser(quiz, user)
        .orElseGet(() -> {
            QuizLeaderboard leaderboard = new QuizLeaderboard();
            leaderboard.setUser(user);
            leaderboard.setQuiz(quiz);
            leaderboard.setPoint(0);
            leaderboard.setTimeTaken(0L);
            return quizLeaderboardRepo.save(leaderboard);
        });
    }

    public List<QuizLeaderboardResponseDTO> getTopQuizLeaderboard(Long quizId) {
        Quiz quiz = new Quiz();
        quiz.setQuizId(quizId);
        List<QuizLeaderboard> topLeaderboards = quizLeaderboardRepo.findTop30ByQuizOrderByPointDescTimeTakenAsc(quiz);
        
        return topLeaderboards.stream()
            .map(leaderboardMapper::toDto)
            .toList();
    }

    public List<Map<String,Object>> getLeaderboardEntries(Phase phase) {

        List<PhaseLeaderboard> leaderboards = phaseLeaderboardRepo.findByPhaseOrderByPointDescTimeTakenAsc(phase);

        System.out.println("Number of Leaderboards: " + leaderboards.size());
        System.out.println("===== Raw Leaderboard Data =====");
        for (PhaseLeaderboard leaderboard : leaderboards) {
            String username = leaderboard.getUser() != null ? leaderboard.getUser().getUsername() : "null";
            System.out.println("User: " + username +
                    ", Point: " + leaderboard.getPoint() );
                }
        System.out.println("================================");

        
        List<Map<String, Object>> formattedLeaderboards = new ArrayList<>();
        int rank = 1; // Initialize rank
        for (PhaseLeaderboard leaderboard : leaderboards) {
            Map<String, Object> entry = new HashMap<>();

            String username = leaderboard.getUser() != null ? leaderboard.getUser().getUsername() : "Unknown";
            String phaseName = leaderboard.getPhase() != null ? leaderboard.getPhase().getName() : "Unknown Quiz";

            entry.put("Rank",  rank); 
            entry.put("username", username); 
            entry.put("score", leaderboard.getPoint()); 
            entry.put("phaseName", phaseName);
            formattedLeaderboards.add(entry);
            rank++;
        }

        System.out.println("===== Formatted Leaderboard Entries =====");
        for (Map<String, Object> entry : formattedLeaderboards) {
            System.out.println("Rank: " + entry.get("rank") + "Username: " + entry.get("username") +
                    ", Score: " + entry.get("score") +
                    ", Phase Name: " + entry.get("phaseName"));
        }
        System.out.println("=========================================");

        return formattedLeaderboards;
    }

    
}
