package com.example.quiz.dto;

import java.time.LocalDateTime;

public record QuizAttemptResponseDTO(
    Long attemptId,
    Long quizId,
    String quizTitle,
    int score,
    int totalQuestions,
    int elapsedSeconds,
    LocalDateTime completedAt
) {}
