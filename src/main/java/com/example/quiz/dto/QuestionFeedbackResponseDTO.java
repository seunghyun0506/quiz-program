package com.example.quiz.dto;

import java.util.List;

public record QuestionFeedbackResponseDTO(
    boolean isCorrect,
    List<Long> correctOptionIds,
    String explanation
) {}
