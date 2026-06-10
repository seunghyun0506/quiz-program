package com.example.quiz.dto;

import java.time.LocalDateTime;
import java.util.List;

public record QuizResponseDTO(
    Long id,
    String title,
    String description,
    String subject,
    String creatorNickname,
    String creatorEmail,
    LocalDateTime createdAt,
    List<QuestionResponseDTO> questions
) {}
