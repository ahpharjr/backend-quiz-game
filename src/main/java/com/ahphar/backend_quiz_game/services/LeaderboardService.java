package com.ahphar.backend_quiz_game.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.Set;
import java.util.UUID;
import java.util.Optional;
import java.util.Objects;

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

    private final RedisTemplate<String, String> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

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

    @Cacheable(value = "phaseLeaderboard", key = "#phaseId")
    public List<PhaseLeaderboardResponseDTO> getTopPhaseLeaderboard(Long phaseId) {
        
        long startTime = System.currentTimeMillis();
        
        Phase phase = new Phase();
        phase.setPhaseId(phaseId);
        List<PhaseLeaderboard> topLeaderboards = phaseLeaderboardRepo.findTop30ByPhaseOrderByPointDescTimeTakenAsc(phase);
        
        List<PhaseLeaderboardResponseDTO> result = topLeaderboards.stream()
            .map(leaderboardMapper::toDto)
            .toList();

        System.out.println("Execution time::>>> " + (System.currentTimeMillis() - startTime) + " ms");
        return result;
    }

    @CacheEvict(value = "phaseLeaderboard", key = "#phaseLeaderboard.phase.phaseId")
    public void updateAndEvictPhaseLeaderboard(PhaseLeaderboard phaseLeaderboard, int additionalPoints, long additionalTime) {

        phaseLeaderboard.setPoint(phaseLeaderboard.getPoint() + additionalPoints);
        phaseLeaderboard.setTimeTaken(phaseLeaderboard.getTimeTaken() + additionalTime);
        PhaseLeaderboard updated = phaseLeaderboardRepo.save(phaseLeaderboard);

        updatePhaseLeaderboardRedis(updated);
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

    public void updateQuizLeaderboardRedis(QuizLeaderboard leaderboard) {
        String key = "quiz:leaderboard:" + leaderboard.getQuiz().getQuizId();
        String value = leaderboard.getUser().getUserId().toString();

        // Check if Redis is already populated for this quiz
        Long existingCount = redisTemplate.opsForZSet().size(key);
        if (existingCount == null || existingCount == 0) {
            // Redis doesn't have data, preload top 30 from DB
            preloadTop30ToRedis(leaderboard.getQuiz());
        }

        // Composite score: higher point, lower time is better
        double compositeScore = leaderboard.getPoint() * 10000 - leaderboard.getTimeTaken();

        // Add/update Redis sorted set
        redisTemplate.opsForZSet().add(key, value, compositeScore);

        //Keep only top 30
        redisTemplate.opsForZSet().removeRange(key, 30, -1);

        //(Optional) Expire after 1 day
        redisTemplate.expire(key, Duration.ofDays(1));

        //Get top 10 for WebSocket push
        Set<String> topUserIds = redisTemplate.opsForZSet().reverseRange(key, 0, 29);
        if (topUserIds == null) return;

        List<QuizLeaderboardResponseDTO> topDtos = topUserIds.stream()
            .map(userIdStr -> {
                try {
                    UUID userId = UUID.fromString(userIdStr);
                    Optional<QuizLeaderboard> entryOpt = quizLeaderboardRepo.findByQuizAndUser(
                        leaderboard.getQuiz(), new User(userId));
                    return entryOpt.map(leaderboardMapper::toDto).orElse(null);
                } catch (IllegalArgumentException e) {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .toList();

        messagingTemplate.convertAndSend("/topic/leaderboard/quizzes/" + leaderboard.getQuiz().getQuizId(), topDtos);

    }


    private void preloadTop30ToRedis(Quiz quiz) {
        String key = "quiz:leaderboard:" + quiz.getQuizId();

        List<QuizLeaderboard> top30 = quizLeaderboardRepo.findTop30ByQuizOrderByPointDescTimeTakenAsc(quiz);
        if (top30.isEmpty()) return;

        for (QuizLeaderboard lb : top30) {
            String userId = lb.getUser().getUserId().toString();
            double score = lb.getPoint() * 10000 - lb.getTimeTaken();
            redisTemplate.opsForZSet().add(key, userId, score);
        }
    }

    public void updatePhaseLeaderboardRedis(PhaseLeaderboard leaderboard){
        String key = "phase:leaderboard:" + leaderboard.getPhase().getPhaseId();
        String userId = leaderboard.getUser().getUserId().toString();

        Long count = redisTemplate.opsForZSet().size(key);
        if(count == null || count == 0){
            preloadTop30ToRedis(leaderboard.getPhase());
        }

        double score = leaderboard.getPoint() * 10000 - (leaderboard.getTimeTaken()/100);
        redisTemplate.opsForZSet().add(key, userId, score);
        redisTemplate.opsForZSet().removeRange(key, 30, -1);
        redisTemplate.expire(key, Duration.ofDays(1));

        Set<String> topUserIds = redisTemplate.opsForZSet().reverseRange(key, 0, 29);

        if(topUserIds == null) return;

        List<PhaseLeaderboardResponseDTO> topDtos = topUserIds.stream()
            .map(uidStr -> {
                try {
                    UUID uid = UUID.fromString(uidStr);
                    Optional<PhaseLeaderboard> entry = phaseLeaderboardRepo.findByPhaseAndUser(leaderboard.getPhase(), new User(uid));
                    return entry.map(leaderboardMapper::toDto).orElse(null);
                } catch (IllegalArgumentException e) {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .toList();

        messagingTemplate.convertAndSend("/topic/leaderboard/phases/" + leaderboard.getPhase().getPhaseId(), topDtos);

        System.out.println("Send message to client ::" + topDtos);
    }

    private void preloadTop30ToRedis(Phase phase){
        String key = "phase:leaderboard:"+ phase.getPhaseId();
        List<PhaseLeaderboard> top30 = phaseLeaderboardRepo.findTop30ByPhaseOrderByPointDescTimeTakenAsc(phase);
        for (PhaseLeaderboard lb : top30){
            String userId = lb.getUser().getUserId().toString();
            double score = lb.getPoint() * 10000 - (lb.getTimeTaken()/100);
            redisTemplate.opsForZSet().add(key, userId, score);
        }
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
