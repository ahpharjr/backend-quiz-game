package com.ahphar.backend_quiz_game.services;

import com.ahphar.backend_quiz_game.DTO.SubmitRequestDTO;
import com.ahphar.backend_quiz_game.models.Quiz;
import com.ahphar.backend_quiz_game.models.User;
import com.ahphar.backend_quiz_game.models.UserProfile;
import com.ahphar.backend_quiz_game.repositories.QuizRepository;
import com.ahphar.backend_quiz_game.repositories.UserProfileRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final QuizRepository quizRepository;

    public void createDefaultProfile(User user){
        UserProfile profile = new UserProfile();
        profile.setUser(user); // Uses @MapsId to assign userIf

        //Default values
        profile.setLevel(1);
        profile.setUserXp(0);
        profile.setTimeSpent(0L);
        profile.setQuizSet(0);
        profile.setHighestScore(0);
        profile.setProfilePicture("profile1.png");
        profile.setCurrentPhase(1);
        //profile.setCurrentQuizSet(1);

        userProfileRepository.save(profile);
    }

    public void updateProfileOnQuizSubmission(User user, Quiz quiz, SubmitRequestDTO dto, boolean isNewQuizAttempt, double existingCorrectPercentage) {
        
        UserProfile profile = user.getProfile();

        // Always update time spent and highest score
        profile.setTimeSpent(profile.getTimeSpent() + dto.getTimeTaken());

        if (dto.getPoint() > profile.getHighestScore()) {
            profile.setHighestScore(dto.getPoint());
        }

        int bonusXp = 0;

        if (isNewQuizAttempt) {
            // Full bonus for first attempt
            bonusXp = (int) Math.round(dto.getCorrectPercentage());
            profile.setUserXp(profile.getUserXp() + 200 + bonusXp);
            profile.setQuizSet(profile.getQuizSet() + 1); // +1 QuizSet

            updateLevel(profile); 

            // Check if all quizzes in current phase are completed
            boolean allQuizzesCompleted = quizRepository
                .findByTopic_Phase_PhaseId(quiz.getTopic().getPhase().getPhaseId())
                .stream()
                .allMatch(q -> q.getQuizLeaderboards().stream()
                    .anyMatch(lb -> lb.getUser().getUserId().equals(user.getUserId())));

            if (allQuizzesCompleted) {
                profile.setCurrentPhase(profile.getCurrentPhase() + 1);
            }
        } else if (dto.getCorrectPercentage() > existingCorrectPercentage) {
            System.out.println("inside");
            // Bonus only for improvement on re-attempt
            bonusXp = (int) Math.round(dto.getCorrectPercentage() - existingCorrectPercentage);
            profile.setUserXp(profile.getUserXp() + bonusXp);

            updateLevel(profile); 
        }
    }

    private void updateLevel(UserProfile profile) {
        int xp = profile.getUserXp();
        if (xp < 2000) {
            profile.setLevel(1);
        } else if (xp < 4000) {
            profile.setLevel(2);
        } else if (xp < 7000){
            profile.setLevel(3);
        } else {
            profile.setLevel(4);
        }
    }

}
