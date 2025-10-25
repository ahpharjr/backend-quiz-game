package com.ahphar.backend_quiz_game.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ahphar.backend_quiz_game.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>{
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<User> findAllByOrderByRegisteredTimeDesc();

    @EntityGraph(attributePaths = {"profile"})
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email); 

}
