package com.example.quiz.dto;

public record QuestionOptionResponseDTO(
    Long id,
    String optionText,
    boolean isCorrect
) {}
