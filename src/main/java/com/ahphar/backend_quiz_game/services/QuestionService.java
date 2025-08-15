package com.ahphar.backend_quiz_game.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahphar.backend_quiz_game.DTO.QuestionRequestDTO;
import com.ahphar.backend_quiz_game.DTO.QuestionResponseDTO;
import com.ahphar.backend_quiz_game.exception.QuestionNotFoundException;
import com.ahphar.backend_quiz_game.mapper.QuestionMapper;
import com.ahphar.backend_quiz_game.models.Question;
import com.ahphar.backend_quiz_game.repositories.QuestionRepository;
import com.ahphar.backend_quiz_game.models.Quiz;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;
    private final QuizService quizService;

    public List<QuestionResponseDTO> getQuestionsByQuizId(Long quizId) {
        List<Question> questions = questionRepository.findByQuiz_QuizId(quizId);

        return questions.stream()
                .map(questionMapper::toDto)
                .toList();
    }

    public Question getQuestionById(Long questionId){
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException("Question not found with id: " + questionId));
    }

    public void createQuestion(QuestionRequestDTO requestDTO, Long quizId) {
        Question question = questionMapper.toModel(requestDTO);

        Quiz quiz = quizService.getQuizById(quizId);
        question.setQuiz(quiz);
        questionRepository.save(question);
    }

    public void updateQuestion(Long questionId, QuestionRequestDTO requestDTO) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException("Question not found with id: " + questionId));
        
        question.setQuestion(requestDTO.getQuestion());
        question.setImage(requestDTO.getImage());
        
        questionRepository.save(question);
    }

    @Transactional
    public void deleteQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException("Question not found with id: " + questionId));
        
        questionRepository.delete(question);
    }
}
