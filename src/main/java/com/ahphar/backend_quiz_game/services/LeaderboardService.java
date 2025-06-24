package com.ahphar.backend_quiz_game.services;

import java.util.List;

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

@Service
public class LeaderboardService {
    
    private final PhaseLeaderboardRepository phaseLeaderboardRepo;
    private final QuizLeaderboardRepository quizLeaderboardRepo;
    private final LeaderboardMapper leaderboardMapper;

    public LeaderboardService(
        PhaseLeaderboardRepository phaseLeaderboardRepo,
        QuizLeaderboardRepository quizLeaderboardRepo,
        LeaderboardMapper leaderboardMapper) {
        this.phaseLeaderboardRepo = phaseLeaderboardRepo;
        this.quizLeaderboardRepo = quizLeaderboardRepo;
        this.leaderboardMapper = leaderboardMapper;
    }

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

    
}
