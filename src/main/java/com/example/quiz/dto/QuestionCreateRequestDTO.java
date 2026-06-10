package com.example.quiz.dto;

import java.util.List;

public record QuestionCreateRequestDTO(
    String questionText,
    List<String> options,
    List<Integer> correctAnswerIndices,
    String explanation,
    int selectionLimit,
    Integer timeLimitSeconds
) {}
