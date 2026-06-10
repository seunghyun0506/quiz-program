package com.example.quiz.service;

import com.example.quiz.domain.*;
import com.example.quiz.dto.*;
import com.example.quiz.repository.QuizAttemptRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuizAttemptService {

    private final QuizAttemptRepository quizAttemptRepository;
    private final QuizService quizService;

    public QuizAttemptService(QuizAttemptRepository quizAttemptRepository, QuizService quizService) {
        this.quizAttemptRepository = quizAttemptRepository;
        this.quizService = quizService;
    }

    public QuestionFeedbackResponseDTO checkSingleQuestion(QuestionFeedbackRequestDTO requestDTO) {
        Quiz quiz = quizService.getQuizEntityById(1L); // Just dummy, we fetch from repository or lookup
        // We will need a way to look up the question directly. Let's make sure we find it from the quiz or load it.
        // Wait, to keep things simple, let's load the quiz by scanning or we can load the question.
        // Wait, we can load the question from the database. Let's write a method in QuizService or load it from the quiz.
        return null;
    }

    // Since we need to look up a question, we can get it from the quiz
    @Transactional(readOnly = true)
    public Question getQuestionInQuiz(Quiz quiz, Long questionId) {
        return quiz.getQuestions().stream()
            .filter(q -> q.getId().equals(questionId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 문제 ID입니다: " + questionId));
    }

    public QuestionFeedbackResponseDTO getFeedback(Long quizId, QuestionFeedbackRequestDTO requestDTO) {
        Quiz quiz = quizService.getQuizEntityById(quizId);
        Question question = getQuestionInQuiz(quiz, requestDTO.questionId());

        List<Long> correctOptionIds = question.getOptions().stream()
            .filter(QuestionOption::isCorrect)
            .map(QuestionOption::getId)
            .collect(Collectors.toList());

        List<Long> selected = requestDTO.selectedOptionIds() != null ? requestDTO.selectedOptionIds() : new ArrayList<>();
        boolean isCorrect = selected.size() == correctOptionIds.size()
            && selected.containsAll(correctOptionIds)
            && correctOptionIds.containsAll(selected);

        return new QuestionFeedbackResponseDTO(isCorrect, correctOptionIds, question.getExplanation());
    }

    public QuizAttempt submitQuiz(QuizSubmitRequestDTO submitDTO, User user) {
        Quiz quiz = quizService.getQuizEntityById(submitDTO.quizId());
        
        int score = 0;
        List<QuizAttemptAnswer> answers = new ArrayList<>();

        for (QuestionAnswerRequestDTO answerDTO : submitDTO.answers()) {
            Question question = getQuestionInQuiz(quiz, answerDTO.questionId());
            List<Long> selectedOptionIds = answerDTO.selectedOptionIds() != null 
                ? answerDTO.selectedOptionIds() 
                : new ArrayList<>();

            List<Long> correctOptionIds = question.getOptions().stream()
                .filter(QuestionOption::isCorrect)
                .map(QuestionOption::getId)
                .collect(Collectors.toList());

            boolean isCorrect = selectedOptionIds.size() == correctOptionIds.size()
                && selectedOptionIds.containsAll(correctOptionIds)
                && correctOptionIds.containsAll(selectedOptionIds);

            if (isCorrect) {
                score++;
            }

            QuizAttemptAnswer attemptAnswer = new QuizAttemptAnswer(question, selectedOptionIds, isCorrect);
            answers.add(attemptAnswer);
        }

        QuizAttempt attempt = new QuizAttempt(
            quiz,
            user,
            score,
            quiz.getQuestions().size(),
            submitDTO.elapsedSeconds(),
            LocalDateTime.now()
        );

        for (QuizAttemptAnswer ans : answers) {
            attempt.addAnswer(ans);
        }

        return quizAttemptRepository.save(attempt);
    }

    @Transactional(readOnly = true)
    public List<QuizAttemptResponseDTO> getUserHistory(User user) {
        return quizAttemptRepository.findByUserOrderByCompletedAtDesc(user).stream()
            .map(attempt -> new QuizAttemptResponseDTO(
                attempt.getId(),
                attempt.getQuiz().getId(),
                attempt.getQuiz().getTitle(),
                attempt.getScore(),
                attempt.getTotalQuestions(),
                attempt.getElapsedSeconds(),
                attempt.getCompletedAt()
            ))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public QuizAttempt getAttemptEntityById(Long attemptId) {
        return quizAttemptRepository.findById(attemptId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 풀이 기록입니다. ID: " + attemptId));
    }
}
