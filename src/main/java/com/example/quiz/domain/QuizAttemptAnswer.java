package com.example.quiz.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quiz_attempt_answers")
public class QuizAttemptAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_attempt_id", nullable = false)
    private QuizAttempt quizAttempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "attempt_answer_options", joinColumns = @JoinColumn(name = "attempt_answer_id"))
    @Column(name = "option_id")
    private List<Long> selectedOptionIds = new ArrayList<>();

    @Column(nullable = false)
    private boolean isCorrect;

    public QuizAttemptAnswer() {
    }

    public QuizAttemptAnswer(Question question, List<Long> selectedOptionIds, boolean isCorrect) {
        this.question = question;
        this.selectedOptionIds = selectedOptionIds;
        this.isCorrect = isCorrect;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuizAttempt getQuizAttempt() {
        return quizAttempt;
    }

    public void setQuizAttempt(QuizAttempt quizAttempt) {
        this.quizAttempt = quizAttempt;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<Long> getSelectedOptionIds() {
        return selectedOptionIds;
    }

    public void setSelectedOptionIds(List<Long> selectedOptionIds) {
        this.selectedOptionIds = selectedOptionIds;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
