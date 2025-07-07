package com.ahphar.backend_quiz_game.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationCodeEmail(String email, String code){
        String subject = "Verify your email";
        String content = "<html><body>" +
            "<p>Hi there,</p>" +
            "<p>We noticed a login attempt on your account. If that was you, please enter the verification code below:</p>" +
            "<p style='font-size: 28px; font-weight: bold; color: #2B7A78; letter-spacing: 2px;'>" + code + "</p>" +
            "<p>If you were not trying to log in to the QuizGame app, please reset your password.</p>" +
            "<p>Best regards,<br>QuizGame Team</p>" +
            "</body></html>";

        // String content = "Hi there, \n\n We noticed a suspicious log-in on your account. if that was you, enter this code: \n"
        //                 + code + "\n If you were not trying to log in to QuizGame app, please reset your password.\n\nBest, \nQuizGame";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }     
    }

}
 