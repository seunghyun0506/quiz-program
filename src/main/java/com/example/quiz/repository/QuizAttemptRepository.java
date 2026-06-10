package com.example.quiz.repository;

import com.example.quiz.domain.QuizAttempt;
import com.example.quiz.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    List<QuizAttempt> findByUserOrderByCompletedAtDesc(User user);
}
