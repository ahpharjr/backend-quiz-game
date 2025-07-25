package com.ahphar.backend_quiz_game.repositories;

import com.ahphar.backend_quiz_game.models.Phase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PhaseRepository extends JpaRepository<Phase, Long>{

    Phase findByName(String name);
    
}
