package com.ahphar.backend_quiz_game.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ahphar.backend_quiz_game.models.Topic;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long>{
    List<Topic> findByPhase_PhaseId(Long phaseId);
}
