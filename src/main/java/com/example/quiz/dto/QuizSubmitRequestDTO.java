package com.example.quiz.dto;

import java.util.List;

public record QuizSubmitRequestDTO(
    Long quizId,
    int elapsedSeconds,
    List<QuestionAnswerRequestDTO> answers
) {}
