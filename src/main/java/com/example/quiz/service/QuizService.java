package com.example.quiz.service;

import com.example.quiz.domain.Question;
import com.example.quiz.domain.QuestionOption;
import com.example.quiz.domain.Quiz;
import com.example.quiz.domain.User;
import com.example.quiz.dto.QuestionCreateRequestDTO;
import com.example.quiz.dto.QuestionOptionResponseDTO;
import com.example.quiz.dto.QuestionResponseDTO;
import com.example.quiz.dto.QuizCreateRequestDTO;
import com.example.quiz.dto.QuizResponseDTO;
import com.example.quiz.repository.QuizRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuizService {

    private final QuizRepository quizRepository;

    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public Quiz createQuiz(QuizCreateRequestDTO requestDTO, User creator) {
        Quiz quiz = new Quiz(
            requestDTO.title(),
            requestDTO.description(),
            requestDTO.subject(),
            creator,
            LocalDateTime.now()
        );

        for (QuestionCreateRequestDTO questionDTO : requestDTO.questions()) {
            Question question = new Question(
                questionDTO.questionText(),
                questionDTO.selectionLimit(),
                questionDTO.timeLimitSeconds() != null ? questionDTO.timeLimitSeconds() : 0,
                questionDTO.explanation()
            );

            List<String> optionsText = questionDTO.options();
            List<Integer> correctIndices = questionDTO.correctAnswerIndices();

            for (int i = 0; i < optionsText.size(); i++) {
                boolean isCorrect = correctIndices.contains(i);
                QuestionOption option = new QuestionOption(optionsText.get(i), isCorrect);
                question.addOption(option);
            }

            quiz.addQuestion(question);
        }

        return quizRepository.save(quiz);
    }

    @Transactional(readOnly = true)
    public List<QuizResponseDTO> getAllQuizzes() {
        return quizRepository.findAllWithCreator().stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Quiz getQuizEntityById(Long id) {
        return quizRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 퀴즈입니다. ID: " + id));
    }

    @Transactional(readOnly = true)
    public QuizResponseDTO getQuizById(Long id) {
        Quiz quiz = getQuizEntityById(id);
        return convertToResponseDTO(quiz);
    }

    public void deleteQuiz(Long id, User user) {
        Quiz quiz = getQuizEntityById(id);
        // Only allow creator to delete
        if (!quiz.getCreator().getId().equals(user.getId())) {
            throw new IllegalArgumentException("자신이 작성한 퀴즈만 삭제할 수 있습니다.");
        }
        quizRepository.delete(quiz);
    }

    public QuizResponseDTO convertToResponseDTO(Quiz quiz) {
        List<QuestionResponseDTO> questionDTOs = quiz.getQuestions().stream()
            .map(q -> new QuestionResponseDTO(
                q.getId(),
                q.getQuestionText(),
                q.getOptions().stream()
                    .map(o -> new QuestionOptionResponseDTO(o.getId(), o.getOptionText(), o.isCorrect()))
                    .collect(Collectors.toList()),
                q.getSelectionLimit(),
                q.getTimeLimitSeconds(),
                q.getExplanation()
            ))
            .collect(Collectors.toList());

        return new QuizResponseDTO(
            quiz.getId(),
            quiz.getTitle(),
            quiz.getDescription(),
            quiz.getSubject(),
            quiz.getCreator().getNickname(),
            quiz.getCreator().getEmail(),
            quiz.getCreatedAt(),
            questionDTOs
        );
    }
}
