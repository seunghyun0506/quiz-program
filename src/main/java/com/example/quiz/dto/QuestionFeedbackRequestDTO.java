package com.example.quiz.dto;

import java.util.List;

public record QuestionFeedbackRequestDTO(
    Long questionId,
    List<Long> selectedOptionIds
) {}
