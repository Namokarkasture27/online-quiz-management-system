#  Online Quiz Management System

A full-stack **Mini Project** developed using **Java, Spring Boot, HTML, CSS and MySQL** for conducting and managing online examinations.

---

#  Project Overview
This system provides an automated platform where:

- **Admin/Teacher** can create quizzes and manage questions  
- **Students** can register, login and attempt quizzes  
- System automatically evaluates answers and generates results

---

#  Features

##  Admin Module
- Admin Login Authentication
- Create New Quiz
- Add Multiple Choice Questions
- Set Correct Answers
- Manage Quizzes
- View Student Results

##  Student Module
- Student Registration/Login
- Attempt Available Quizzes
- Submit Quiz
- Auto Score Calculation
- View Result Dashboard

---

#  Tech Stack

| Technology | Used For |
|-----------|----------|
Java | Backend Logic |
Spring Boot | Application Framework |
Spring MVC | Request Handling |
JPA/Hibernate | Database ORM |
MySQL | Database |
HTML | Frontend |
CSS | Styling |
Thymeleaf | Dynamic Templates |

---

#  Project Structure

```bash
online-quiz-management-system/
│
├── src/main/java/com/example/demo
│   ├── controller
│   │   ├── HomeController.java
│   │   ├── AdminController.java
│   │   └── StudentController.java
│   │
│   ├── entity
│   │   ├── Admin.java
│   │   ├── Student.java
│   │   ├── Quiz.java
│   │   ├── Question.java
│   │   └── QuizResult.java
│   │
│   └── repository
│       ├── AdminRepository.java
│       ├── StudentRepository.java
│       ├── QuizRepository.java
│       └── QuestionRepository.java
│
├── resources
│   ├── templates
│   │   ├── admin-dashboard.html
│   │   ├── student-login.html
│   │   └── quiz-page.html
│   │
│   └── static/css
│       └── style.css
```

---

#  System Flow

```text
Admin Login
   ↓
Create Quiz
   ↓
Add Questions
   ↓
Publish Quiz
   ↓
Student Login
   ↓
Attempt Quiz
   ↓
Submit Answers
   ↓
Automatic Evaluation
   ↓
Display Result
```

---

#  Database Entities

- Admin  
- Student  
- Quiz  
- Question  
- QuizResult  

Relationship:

```text
Admin → Creates → Quiz
Quiz → Contains → Questions
Student → Attempts → Quiz
Quiz → Generates → Result
```

---

#  Screenshots
#  Project Screenshots

| Admin Dashboard | Quiz Interface | Result Page |
|----------------|---------------|-------------|
| ![](screenshots/admin-dashboard.png) | ![](screenshots/quiz-interface.png) | ![](screenshots/result-page.png) |

- Home Page  
- Admin Dashboard  
- Quiz Interface  
- Result Page

---

#  Future Enhancements
- Timer Based Quiz
- Leaderboard
- Negative Marking
- Email Notifications
- Role Based Security
- AI-based Question Generation

---

#  Developed By
**Namokar Kasture**

Mini Project | Spring Boot + Java
