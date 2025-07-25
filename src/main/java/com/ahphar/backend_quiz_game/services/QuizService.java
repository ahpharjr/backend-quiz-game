package com.ahphar.backend_quiz_game.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ahphar.backend_quiz_game.DTO.SubmitRequestDTO;
import com.ahphar.backend_quiz_game.DTO.SubmitResponseDTO;
import com.ahphar.backend_quiz_game.DTO.UserProfileDTO;
import com.ahphar.backend_quiz_game.models.Answer;
import com.ahphar.backend_quiz_game.models.Phase;
import com.ahphar.backend_quiz_game.models.PhaseLeaderboard;
import com.ahphar.backend_quiz_game.models.Question;
import com.ahphar.backend_quiz_game.models.Quiz;
import com.ahphar.backend_quiz_game.models.QuizLeaderboard;
import com.ahphar.backend_quiz_game.models.User;
import com.ahphar.backend_quiz_game.models.UserProfile;
import com.ahphar.backend_quiz_game.repositories.AnswerRepository;
import com.ahphar.backend_quiz_game.repositories.PhaseLeaderboardRepository;
import com.ahphar.backend_quiz_game.repositories.QuestionRepository;
import com.ahphar.backend_quiz_game.repositories.QuizLeaderboardRepository;
import com.ahphar.backend_quiz_game.repositories.QuizRepository;
import com.ahphar.backend_quiz_game.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuizRepository quizRepository;
    private final QuizLeaderboardRepository quizLeaderboardRepo;
    private final PhaseLeaderboardRepository phaseLeaderboardRepo;
    private final LeaderboardService leaderboardService;
    private final UserRepository userRepository;
    private final UserProfileService userProfileService;


    public List<Question> getQuestionsByQuizSet(Long quizId) {
        return questionRepository.findByQuiz_QuizId(quizId);
    }

    public List<Answer> getAnswersByQuiz(Long quizId) {
        return answerRepository.findByQuestion_Quiz_QuizId(quizId);
    }

    public Quiz getQuizById(Long quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + quizId));
    }

    public SubmitResponseDTO submitQuiz(User currentUser, Long quizId, SubmitRequestDTO requestDTO) {
        Quiz quiz = getQuizById(quizId);
            // boolean isNew = quizLeaderboardRepo.findByQuizAndUser(quiz, currentUser).isEmpty();
        Phase phase = quiz.getTopic().getPhase();

        Optional<QuizLeaderboard> existingQuizLbOpt = quizLeaderboardRepo.findByQuizAndUser(quiz, currentUser);
        boolean isNewQuizAttempt = existingQuizLbOpt.isEmpty();

        PhaseLeaderboard phaseLeaderboard = leaderboardService.createPhaseLeaderboardIfAbsent(currentUser, phase);

        if (isNewQuizAttempt) {
            handleFirstAttempt(currentUser, quiz, requestDTO, phaseLeaderboard);
        }

        double existingCorrectPercentage = existingQuizLbOpt.map(QuizLeaderboard::getCorrectPercentage).orElse(0.0);
        userProfileService.updateProfileOnQuizSubmission(currentUser, quiz, requestDTO, isNewQuizAttempt, existingCorrectPercentage);

        if(!isNewQuizAttempt) {
            handleImprovedAttempt(existingQuizLbOpt.get(), requestDTO, phaseLeaderboard);
        }

        userRepository.save(currentUser);

        UserProfile profile = currentUser.getProfile();

            return new SubmitResponseDTO(
            "Quiz submitted successfully",
            isNewQuizAttempt,
            new UserProfileDTO(
                profile.getUserXp(),
                profile.getLevel(),
                profile.getTimeSpent(),
                profile.getQuizSet(),
                profile.getHighestScore(),
                profile.getCurrentPhase()
            )
        );
    }

    private void handleFirstAttempt(User user, Quiz quiz, SubmitRequestDTO dto, PhaseLeaderboard phaseLeaderboard) {
        QuizLeaderboard quizLeaderboard = new QuizLeaderboard();
        quizLeaderboard.setUser(user);
        quizLeaderboard.setQuiz(quiz);
        quizLeaderboard.setPoint(dto.getPoint());
        quizLeaderboard.setTimeTaken(dto.getTimeTaken());
        quizLeaderboard.setCorrectPercentage(dto.getCorrectPercentage());
        quizLeaderboardRepo.save(quizLeaderboard);

        updatePhaseLeaderboard(phaseLeaderboard, dto.getPoint(), dto.getTimeTaken());
    }

    private void handleImprovedAttempt(QuizLeaderboard existing, SubmitRequestDTO dto, PhaseLeaderboard phaseLeaderboard) {
        int previousPoints = existing.getPoint();
        long previousTime = existing.getTimeTaken();
        double previousCorrectPercentage = existing.getCorrectPercentage();

        boolean isBetterScore = dto.getPoint() > previousPoints;
        boolean isEqualScoreFasterTime = dto.getPoint() == previousPoints && dto.getTimeTaken() < previousTime;
        boolean isBetterCorrectPercentage = dto.getCorrectPercentage() > previousCorrectPercentage;

        // If no improvement in score, time, or percentage, skip
        if (!isBetterScore && !isEqualScoreFasterTime && !isBetterCorrectPercentage) return;

        // Update quiz leaderboard values
        existing.setPoint(dto.getPoint());
        existing.setTimeTaken(dto.getTimeTaken());

        //  Update correctPercentage if it's improved
        if (isBetterCorrectPercentage) {
            existing.setCorrectPercentage(dto.getCorrectPercentage());
        }

        quizLeaderboardRepo.save(existing);

        //  Update phase leaderboard only for score/time improvements
        if (isBetterScore || isEqualScoreFasterTime) {
            int additionalPoints = dto.getPoint() - previousPoints;
            long additionalTime = dto.getTimeTaken() - previousTime;
            updatePhaseLeaderboard(phaseLeaderboard, additionalPoints, additionalTime);
        }
    }


    private void updatePhaseLeaderboard(PhaseLeaderboard phaseLeaderboard, int additionalPoints, long additionalTime) {
        phaseLeaderboard.setPoint(phaseLeaderboard.getPoint() + additionalPoints);
        phaseLeaderboard.setTimeTaken(phaseLeaderboard.getTimeTaken() + additionalTime);
        phaseLeaderboardRepo.save(phaseLeaderboard);
    }


}
