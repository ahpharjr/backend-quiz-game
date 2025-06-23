package com.ahphar.backend_quiz_game.services;

import com.ahphar.backend_quiz_game.models.User;
import com.ahphar.backend_quiz_game.models.UserProfile;
import com.ahphar.backend_quiz_game.repositories.UserProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public void createDefaultProfile(User user){
        UserProfile profile = new UserProfile();
        profile.setUser(user); // Uses @MapsId to assign userIf

        //Default values
        profile.setLevel(1);
        profile.setUserXp(0);
        profile.setTimeSpent(0L);
        profile.setQuizSet(1);
        profile.setHighestScore(0);
        profile.setProfilePicture("default.png");
        profile.setCurrentPhase(1);
        profile.setCurrentQuizSet(1);

        userProfileRepository.save(profile);
    }

}
