# 🎮 Quiz Game Backend

This is the **backend system** for the **Quiz Game**, an interactive educational application designed to test users’ knowledge across various topics especially for project management. Built using **Spring Boot**, this backend handles user authentication, quiz logic, leaderboard ranking, XP progression, achievement tracking, and more. It is secured with JWT and integrates smoothly with frontend.

---

## 📌 Project Overview

The **Quiz Game Backend** provides a robust and secure RESTful API to support:

- User registration and login (Email + Google OAuth)
- Secure JWT-based authentication
- Email verification with OTP (6-digit code)
- Quiz management (questions, answers, feedback)
- Leaderboard system
- XP system and level progression
- Achievements system
- Flashcard management
- Locked quiz sets with performance requirements

---

## ⚙️ Tech Stack

| Layer         | Technology                     |
|---------------|--------------------------------|
| Language       | Java                       |
| Framework      | Spring Boot                    |
| Security       | Spring Security + JWT          |
| Persistence    | Spring Data JPA, Hibernate     |
| Database       | MYSQL          |
| API Docs       | Springdoc OpenAPI (Swagger)    |
| Email Service  | JavaMailSender                 |
| Token Utility  | JWT (jjwt)                     |
| Build Tool     | Maven                          |

---

## 🔐 Security Features

- **JWT Authentication**: Stateless access to secured endpoints with token-based auth.
- **Email Verification**: OTP-based (6-digit) email confirmation on signup.
- **Password Reset**: Token-based reset mechanism after OTP verification.

---

## 📚 Main Modules

- `AuthController` – Registration, login, Google OAuth, email verification, password reset
- `QuizController` – Quiz fetching, answering, result feedback
- `LeaderboardController` – Rankings based on score and time
- `AchievementController` – Unlock phase-based and XP-based achievements
- `FlashcardController` – Flashcard collection toggling
- `UserController` – User profile, XP, and progress


