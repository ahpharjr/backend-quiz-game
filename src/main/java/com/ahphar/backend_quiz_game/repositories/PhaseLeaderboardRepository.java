package com.ahphar.backend_quiz_game.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ahphar.backend_quiz_game.models.Phase;
import com.ahphar.backend_quiz_game.models.PhaseLeaderboard;
import com.ahphar.backend_quiz_game.models.Quiz;
import com.ahphar.backend_quiz_game.models.User;

@Repository
public interface PhaseLeaderboardRepository extends JpaRepository<PhaseLeaderboard, Long> {
    List<PhaseLeaderboard> findTop30ByPhaseOrderByPointDescTimeTakenAsc(Phase phase);
    List<PhaseLeaderboard> findTop10ByPhase_PhaseIdOrderByPointDescTimeTakenAsc(Long phaseId);
    List<PhaseLeaderboard> findByPhaseOrderByPointDescTimeTakenAsc(Phase phase);

    Optional<PhaseLeaderboard> findByPhaseAndUser(Phase phase, User user);
    Optional<Quiz> findByUserAndPhase(User currentUser, Phase phase);
}
