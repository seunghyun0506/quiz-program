package com.example.quiz.dto;

import java.util.List;

public record QuestionResponseDTO(
    Long id,
    String questionText,
    List<QuestionOptionResponseDTO> options,
    int selectionLimit,
    Integer timeLimitSeconds,
    String explanation
) {}
