package com.ahphar.backend_quiz_game.admin.services;

import org.springframework.stereotype.Service;

import com.ahphar.backend_quiz_game.admin.DTOs.QuizRespDTO;
import com.ahphar.backend_quiz_game.admin.mapper.QuizMapper;
import com.ahphar.backend_quiz_game.models.Quiz;
import com.ahphar.backend_quiz_game.repositories.QuizRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdQuizService {
    
    private final QuizRepository quizRepository;
    private final QuizMapper quizMapper;

    public QuizRespDTO getQuizById(Long quizId) {
        Quiz quiz = quizRepository.getById(quizId);
        return quizMapper.toDTO(quiz);
    }
}
