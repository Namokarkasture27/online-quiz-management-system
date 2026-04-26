package com.example.demo.controller;

import com.example.demo.entity.Question;
import com.example.demo.entity.Quiz;
import com.example.demo.entity.QuizResult;
import com.example.demo.entity.Student;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.repository.QuizRepository;
import com.example.demo.repository.QuizResultRepository;
import com.example.demo.repository.StudentRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizResultRepository quizResultRepository;

    // -------- REGISTER --------
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("student", new Student());
        return "student-register";
    }

    @PostMapping("/register")
    public String doRegister(@ModelAttribute("student") Student student) {
        studentRepository.save(student);
        return "redirect:/student/login";
    }

    // -------- LOGIN --------
    @GetMapping("/login")
    public String showLoginForm() {
        return "student-login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String email,
                          @RequestParam String password,
                          HttpSession session,
                          Model model) {

        Student student = studentRepository.findByEmail(email);
        if (student != null && student.getPassword().equals(password)) {
            session.setAttribute("studentId", student.getId());
            session.setAttribute("studentName", student.getName());
            return "redirect:/student/quizzes";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "student-login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/student/login";
    }

    // -------- LIST QUIZZES --------
    @GetMapping("/quizzes")
    public String listQuizzes(HttpSession session, Model model) {
        if (session.getAttribute("studentId") == null) {
            return "redirect:/student/login";
        }
        model.addAttribute("studentName", session.getAttribute("studentName"));
        model.addAttribute("quizzes", quizRepository.findAll());
        return "student-quizzes";
    }

    // -------- TAKE QUIZ --------
    @GetMapping("/quiz/{id}")
    public String showQuiz(@PathVariable Long id,
                           HttpSession session,
                           Model model) {
        if (session.getAttribute("studentId") == null) {
            return "redirect:/student/login";
        }

        Quiz quiz = quizRepository.findById(id).orElseThrow();
        List<Question> questions = questionRepository.findByQuizId(id);

        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", questions);

        return "student-take-quiz";
    }

    // -------- SUBMIT QUIZ & SHOW RESULT --------
    @PostMapping("/quiz/{id}/submit")
    public String submitQuiz(@PathVariable Long id,
                             @RequestParam Map<String, String> params,
                             HttpSession session,
                             Model model) {

        if (session.getAttribute("studentId") == null) {
            return "redirect:/student/login";
        }

        Long studentId = (Long) session.getAttribute("studentId");
        Student student = studentRepository.findById(studentId).orElseThrow();
        Quiz quiz = quizRepository.findById(id).orElseThrow();
        List<Question> questions = questionRepository.findByQuizId(id);

        int score = 0;
        for (Question q : questions) {
            String key = "q_" + q.getId();       // matches input name in HTML
            String answer = params.get(key);    // A/B/C/D
            if (answer != null && answer.equalsIgnoreCase(q.getCorrectOption())) {
                score++;
            }
        }

        // save result in DB
        QuizResult result = new QuizResult();
        result.setStudent(student);
        result.setQuiz(quiz);
        result.setScore(score);
        result.setTotalQuestions(questions.size());
        result.setAttemptedAt(LocalDateTime.now());
        quizResultRepository.save(result);

        // send to result page
        model.addAttribute("score", score);
        model.addAttribute("total", questions.size());
        model.addAttribute("quizTitle", quiz.getTitle());

        return "student-result";
    }
}

