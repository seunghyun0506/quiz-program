package com.example.quiz.dto;

import java.util.List;

public record QuizCreateRequestDTO(
    String title,
    String description,
    String subject,
    List<QuestionCreateRequestDTO> questions
) {}
