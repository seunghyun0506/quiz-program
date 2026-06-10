package com.example.quiz.controller;

import com.example.quiz.domain.User;
import com.example.quiz.dto.QuizCreateRequestDTO;
import com.example.quiz.dto.QuizResponseDTO;
import com.example.quiz.service.QuizService;
import com.example.quiz.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@Controller
public class QuizController {

    private final QuizService quizService;
    private final UserService userService;

    public QuizController(QuizService quizService, UserService userService) {
        this.quizService = quizService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/quizzes";
    }

    @GetMapping("/quizzes")
    public String listQuizzes(Model model, Principal principal) {
        List<QuizResponseDTO> quizzes = quizService.getAllQuizzes();
        model.addAttribute("quizzes", quizzes);
        
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("currentUser", user);
        return "quiz/list";
    }

    @GetMapping("/quizzes/create")
    public String createQuizPage() {
        return "quiz/create";
    }

    @PostMapping("/quizzes/create")
    @ResponseBody
    public ResponseEntity<String> createQuiz(@RequestBody QuizCreateRequestDTO requestDTO, Principal principal) {
        try {
            User user = userService.findByEmail(principal.getName());
            quizService.createQuiz(requestDTO, user);
            return ResponseEntity.ok("퀴즈가 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("퀴즈 등록에 실패했습니다: " + e.getMessage());
        }
    }

    @GetMapping("/quizzes/{id}")
    public String quizDetail(@PathVariable Long id, Model model) {
        QuizResponseDTO quiz = quizService.getQuizById(id);
        model.addAttribute("quiz", quiz);
        return "quiz/detail";
    }

    @PostMapping("/quizzes/{id}/delete")
    public String deleteQuiz(@PathVariable Long id, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        quizService.deleteQuiz(id, user);
        return "redirect:/quizzes";
    }
}
