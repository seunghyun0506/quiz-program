package com.example.quiz.repository;

import com.example.quiz.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    @Query("SELECT q FROM Quiz q JOIN FETCH q.creator ORDER BY q.createdAt DESC")
    List<Quiz> findAllWithCreator();
}
