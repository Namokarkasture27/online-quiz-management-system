package com.example.demo.controller;

import com.example.demo.entity.Admin;
import com.example.demo.entity.Question;
import com.example.demo.entity.Quiz;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.repository.QuizRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    // -------- LOGIN --------
    @GetMapping("/login")
    public String showLoginForm() {
        return "admin-login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          Model model,
                          HttpSession session) {

        Admin admin = adminRepository.findByUsername(username);

        if (admin != null && admin.getPassword().equals(password)) {
            session.setAttribute("adminUser", admin.getUsername());
            return "redirect:/admin/dashboard";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "admin-login";
        }
    }

    // -------- DASHBOARD --------
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login";
        }
        return "admin-dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }

    // -------- QUIZ LIST --------
    @GetMapping("/quizzes")
    public String listQuizzes(HttpSession session, Model model) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login";
        }
        model.addAttribute("quizzes", quizRepository.findAll());
        return "admin-quizzes";
    }

    // -------- CREATE QUIZ --------
    @GetMapping("/quiz/new")
    public String showCreateQuizForm(HttpSession session, Model model) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login";
        }
        model.addAttribute("quiz", new Quiz());
        return "admin-create-quiz";
    }

    @PostMapping("/quiz/save")
    public String saveQuiz(HttpSession session, Quiz quiz) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login";
        }
        quizRepository.save(quiz);
        return "redirect:/admin/quizzes";
    }

    // -------- QUESTIONS FOR ONE QUIZ --------
    @GetMapping("/quiz/{id}/questions")
    public String showQuizQuestions(@PathVariable Long id,
                                    HttpSession session,
                                    Model model) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login";
        }

        Quiz quiz = quizRepository.findById(id).orElseThrow();
        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", questionRepository.findByQuizId(id));
        model.addAttribute("question", new Question());

        return "admin-quiz-questions";
    }


    @PostMapping("/quiz/{id}/questions/save")
    public String saveQuestion(@PathVariable Long id,
                               HttpSession session,
                               @ModelAttribute("question") Question question) {
        if (session.getAttribute("adminUser") == null) {
            return "redirect:/admin/login";
        }

        // Always treat it as NEW question (force INSERT, not UPDATE)
        question.setId(null);

        Quiz quiz = quizRepository.findById(id).orElseThrow();
        question.setQuiz(quiz);

        questionRepository.save(question);

        return "redirect:/admin/quiz/" + id + "/questions";
    }


}
