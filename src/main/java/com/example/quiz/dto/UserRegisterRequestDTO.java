package com.example.quiz.dto;

public record UserRegisterRequestDTO(
    String email,
    String password,
    String nickname
) {}
