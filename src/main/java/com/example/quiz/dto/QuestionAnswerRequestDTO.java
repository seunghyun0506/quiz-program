package com.example.quiz.dto;

import java.util.List;

public record QuestionAnswerRequestDTO(
    Long questionId,
    List<Long> selectedOptionIds
) {}
