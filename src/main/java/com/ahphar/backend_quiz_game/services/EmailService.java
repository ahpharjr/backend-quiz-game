package com.ahphar.backend_quiz_game.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    
    private final JavaMailSender mailSender;

    public void sendVerificationCodeEmail(String email, String code) {
        String subject = "Verify your email";
        String content = buildEmailContent(
            "We noticed a login attempt on your account. If that was you, please enter the verification code below:",
            code,
            "If you were not trying to log in to the QuizGame app, please reset your password."
        );
        sendEmail(email, subject, content);
    }

    public void sendResetPasswordCode(String email, String code) {
        String subject = "Reset your QuizGame password";
        String content = buildEmailContent(
            "We received a request to reset your password. Use the code below to proceed:",
            code,
            "If you did not request a password reset, please ignore this email."
        );
        sendEmail(email, subject, content);
    }

    private void sendEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private String buildEmailContent(String introMessage, String code, String footerMessage) {
        return "<html><body>" +
               "<p>Hi there,</p>" +
               "<p>" + introMessage + "</p>" +
               "<p style='font-size: 28px; font-weight: bold; color: #2B7A78; letter-spacing: 2px;'>" + code + "</p>" +
               "<p>" + footerMessage + "</p>" +
               "<p>Best regards,<br>QuizGame Team</p>" +
               "</body></html>";
    }
}
