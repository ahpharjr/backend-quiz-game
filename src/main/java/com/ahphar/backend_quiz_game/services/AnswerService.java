package com.ahphar.backend_quiz_game.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ahphar.backend_quiz_game.DTO.AnswerRequestDTO;
import com.ahphar.backend_quiz_game.DTO.AnswerResponseDTO;
import com.ahphar.backend_quiz_game.exception.AnswerNotFoundException;
import com.ahphar.backend_quiz_game.mapper.AnswerMapper;
import com.ahphar.backend_quiz_game.models.Answer;
import com.ahphar.backend_quiz_game.repositories.AnswerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerService {
    
    private final AnswerRepository answerRepository;
    private final AnswerMapper answerMapper;
    private final QuestionService questionService;

    public List<AnswerResponseDTO> getAllAnswersByQuizId(Long quizId){
        List<Answer> answers = answerRepository.findByQuestion_Quiz_QuizId(quizId);

        return answers.stream()
                .map(answerMapper::toDto)
                .toList();
    }

    public List<AnswerResponseDTO> getAllAnswersByQuestionId(Long questionId){
        List<Answer> answers = answerRepository.findByQuestion_QuestionId(questionId);

        return answers.stream()
                .map(answerMapper::toDto)
                .toList();
    }

    public void createAnswer(Long questionId, AnswerRequestDTO requestDto){
        Answer answer = answerMapper.toModel(requestDto);
        answer.setQuestion(questionService.getQuestionById(questionId));
        answerRepository.save(answer);
    }

    public void updateAnswer(Long answerId, AnswerRequestDTO dto){
        Answer answer = answerRepository.findById(answerId)
                        .orElseThrow(() -> new AnswerNotFoundException("Answer not found!"));

        answer.setAnswer(dto.getAnswer());
        answer.setCorrect(dto.getIsCorrect());

        answerRepository.save(answer);
    }

    public void deleteAnswer(Long answerId){
        Answer answer = answerRepository.findById(answerId)
                        .orElseThrow(() -> new AnswerNotFoundException("Answer not found!"));

        answerRepository.delete(answer);
    }
}
