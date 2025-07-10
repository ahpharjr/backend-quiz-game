package com.ahphar.backend_quiz_game.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ahphar.backend_quiz_game.DTO.AchievementResponseDTO;
import com.ahphar.backend_quiz_game.DTO.AchievementStatusDTO;
import com.ahphar.backend_quiz_game.mapper.AchievementMapper;
import com.ahphar.backend_quiz_game.models.Achievement;
import com.ahphar.backend_quiz_game.models.PhaseLeaderboard;
import com.ahphar.backend_quiz_game.models.User;
import com.ahphar.backend_quiz_game.models.UserAchievement;
import com.ahphar.backend_quiz_game.repositories.AchievementRepository;
import com.ahphar.backend_quiz_game.repositories.PhaseLeaderboardRepository;
import com.ahphar.backend_quiz_game.repositories.QuizLeaderboardRepository;
import com.ahphar.backend_quiz_game.repositories.QuizRepository;
import com.ahphar.backend_quiz_game.repositories.UserAchievementRepository;
import com.ahphar.backend_quiz_game.util.AchievementCodes;

@Service
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final AchievementMapper achievementMapper;
    private final UserAchievementRepository userAchievementRepo;
    private final QuizRepository quizRepo;
    private final QuizLeaderboardRepository quizLeaderboardRepository;
    private final PhaseLeaderboardRepository phaseLeaderboardRepository;

    public AchievementService(AchievementRepository achievementRepository, AchievementMapper achievementMapper,
                                UserAchievementRepository userAchievementRepository, QuizRepository quizRepo,
                                QuizLeaderboardRepository quizLeaderboardRepository, PhaseLeaderboardRepository phaseLeaderboardRepository){
        this.achievementRepository = achievementRepository;
        this.achievementMapper = achievementMapper;
        this.userAchievementRepo = userAchievementRepository;
        this.quizRepo = quizRepo;
        this.quizLeaderboardRepository = quizLeaderboardRepository;
        this.phaseLeaderboardRepository = phaseLeaderboardRepository;
    }
    
    public List<AchievementResponseDTO> getAllAchievements(){
        List<Achievement> achievements = achievementRepository.findAll();

        return achievements.stream()
            .map(achievementMapper::toDto)
            .collect(Collectors.toList());
    }

    public List<UserAchievement> getUserAchievements(User user){
        List<UserAchievement> achievements = userAchievementRepo.findByUser(user);

        return achievements;
    }

    public List<AchievementStatusDTO> getAchievementStatusDTOs(User user){
                List<Achievement> all = achievementRepository.findAll();
        List<String> unlockedCodes = userAchievementRepo.findByUser(user).stream()
            .map(ua -> ua.getAchievement().getCode())
            .toList();

        List<AchievementStatusDTO> response = all.stream()
            .map(a -> new AchievementStatusDTO(
                a.getCode(),
                a.getTitle(),
                a.getImageUrl(),
                a.getDescription(),
                unlockedCodes.contains(a.getCode())
            ))
            .toList();

        return response;
    }

    public void evaluateAchievements(User user) {
        unlockPhaseAchievements(user);
        unlockRankAchievement(user);
        unlockXpBasedAchievements(user);
    }

    private void unlockPhaseAchievements(User user) {
        for (long phase = 1; phase <= 4; phase++) {
            boolean allCompleted = quizRepo.findByTopic_Phase_PhaseId(phase).stream()
                .allMatch(quiz -> quizLeaderboardRepository.existsByUserAndQuiz(user, quiz));

            String code = "PHASE_" + phase;

            if (allCompleted && !hasAchievement(user, code)) {
                unlock(user, code);
            }
        }
    }

    private void unlockRankAchievement(User user) {
        for (long phase = 1; phase <= 4; phase++) {
            List<PhaseLeaderboard> topRankers = phaseLeaderboardRepository.findTop10ByPhase_PhaseIdOrderByPointDescTimeTakenAsc(phase);

            if (!topRankers.isEmpty() && 
                topRankers.get(0).getUser().getUserId().equals(user.getUserId())) {

                unlock(user, AchievementCodes.FIRST_ACHIEVER);
                break; // already unlocked, no need to continue
            }
        }
    }

    private void unlockXpBasedAchievements(User user) {
        int xp = user.getProfile().getUserXp();
        if (xp >= 1200) unlock(user, AchievementCodes.RISING_STAR);
        if (xp >= 3000) unlock(user, AchievementCodes.ROOKIE_QUIZZER);
        if (xp >= 5000) unlock(user, AchievementCodes.LEGEND);
    }

    private boolean hasAchievement(User user, String code) {
        return userAchievementRepo.existsByUserAndAchievement_Code(user, code);
    }

    private void unlock(User user, String code) {
        Achievement achievement = achievementRepository.findByCode(code)
            .orElseThrow(() -> new RuntimeException("Achievement not found: " + code));

        if (!hasAchievement(user, code)) {
            userAchievementRepo.save(new UserAchievement(null, user, achievement, LocalDateTime.now()));
        }
    }
}