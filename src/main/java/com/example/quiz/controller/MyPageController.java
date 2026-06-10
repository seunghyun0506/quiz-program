package com.example.quiz.controller;

import com.example.quiz.domain.QuizAttempt;
import com.example.quiz.domain.QuizAttemptAnswer;
import com.example.quiz.domain.User;
import com.example.quiz.dto.QuizAttemptResponseDTO;
import com.example.quiz.service.QuizAttemptService;
import com.example.quiz.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

    private final UserService userService;
    private final QuizAttemptService quizAttemptService;

    public MyPageController(UserService userService, QuizAttemptService quizAttemptService) {
        this.userService = userService;
        this.quizAttemptService = quizAttemptService;
    }

    @GetMapping
    public String myPageHome(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        List<QuizAttemptResponseDTO> history = quizAttemptService.getUserHistory(user);

        // Calculate simple statistics
        int totalAttempts = history.size();
        double averageScore = 0;
        if (totalAttempts > 0) {
            double sumRatio = history.stream()
                .mapToDouble(h -> (double) h.score() / h.totalQuestions() * 100)
                .sum();
            averageScore = Math.round(sumRatio / totalAttempts * 10) / 10.0;
        }

        model.addAttribute("history", history);
        model.addAttribute("totalAttempts", totalAttempts);
        model.addAttribute("averageScore", averageScore);
        model.addAttribute("user", user);
        return "mypage/history";
    }

    @GetMapping("/wrong-answers")
    public String wrongAnswersNote(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        // Let's actually query the database via attempts history
        List<QuizAttemptResponseDTO> history = quizAttemptService.getUserHistory(user);
        
        List<QuizAttemptAnswer> wrongAnswers = new ArrayList<>();
        for (QuizAttemptResponseDTO h : history) {
            QuizAttempt attempt = quizAttemptService.getAttemptEntityById(h.attemptId());
            for (QuizAttemptAnswer ans : attempt.getAnswers()) {
                if (!ans.isCorrect()) {
                    wrongAnswers.add(ans);
                }
            }
        }

        model.addAttribute("wrongAnswers", wrongAnswers);
        return "mypage/wrong-answers";
    }
}
