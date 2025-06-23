package com.ahphar.backend_quiz_game.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ahphar.backend_quiz_game.models.Phase;
import com.ahphar.backend_quiz_game.repositories.PhaseRepository;

@RestController
@RequestMapping("/phases")
public class PhaseController {

    @Autowired
    private PhaseRepository phaseRepository;
    
    @GetMapping
    public ResponseEntity<List<Phase>> getAllPhases(){
        List<Phase> phases = phaseRepository.findAll();
        return ResponseEntity.ok(phases);
    }
}
