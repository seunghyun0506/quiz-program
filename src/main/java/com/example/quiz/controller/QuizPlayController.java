package com.example.quiz.controller;

import com.example.quiz.domain.QuizAttempt;
import com.example.quiz.domain.User;
import com.example.quiz.dto.QuestionFeedbackRequestDTO;
import com.example.quiz.dto.QuestionFeedbackResponseDTO;
import com.example.quiz.dto.QuizResponseDTO;
import com.example.quiz.dto.QuizSubmitRequestDTO;
import com.example.quiz.service.QuizAttemptService;
import com.example.quiz.service.QuizService;
import com.example.quiz.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
public class QuizPlayController {

    private final QuizService quizService;
    private final QuizAttemptService quizAttemptService;
    private final UserService userService;

    public QuizPlayController(QuizService quizService, QuizAttemptService quizAttemptService, UserService userService) {
        this.quizService = quizService;
        this.quizAttemptService = quizAttemptService;
        this.userService = userService;
    }

    @GetMapping("/quizzes/{id}/play")
    public String playQuiz(@PathVariable Long id, @RequestParam(defaultValue = "feedback") String mode, Model model) {
        QuizResponseDTO quiz = quizService.getQuizById(id);
        model.addAttribute("quiz", quiz);
        model.addAttribute("mode", mode);
        return "quiz/play";
    }

    @PostMapping("/quizzes/{id}/feedback")
    @ResponseBody
    public ResponseEntity<QuestionFeedbackResponseDTO> getQuestionFeedback(
            @PathVariable Long id,
            @RequestBody QuestionFeedbackRequestDTO requestDTO) {
        QuestionFeedbackResponseDTO feedback = quizAttemptService.getFeedback(id, requestDTO);
        return ResponseEntity.ok(feedback);
    }

    @PostMapping("/quizzes/{id}/submit")
    @ResponseBody
    public ResponseEntity<Long> submitQuiz(
            @PathVariable Long id,
            @RequestBody QuizSubmitRequestDTO requestDTO,
            Principal principal) {
        User user = userService.findByEmail(principal.getName());
        QuizAttempt attempt = quizAttemptService.submitQuiz(requestDTO, user);
        return ResponseEntity.ok(attempt.getId());
    }

    @GetMapping("/quizzes/attempts/{attemptId}/result")
    public String attemptResult(@PathVariable Long attemptId, Model model, Principal principal) {
        QuizAttempt attempt = quizAttemptService.getAttemptEntityById(attemptId);
        
        // Ensure user owns this attempt
        if (!attempt.getUser().getEmail().equals(principal.getName())) {
            return "redirect:/quizzes";
        }

        model.addAttribute("attempt", attempt);
        model.addAttribute("quiz", quizService.convertToResponseDTO(attempt.getQuiz()));
        return "quiz/result";
    }
}
