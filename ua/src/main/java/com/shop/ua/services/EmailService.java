package com.shop.ua.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendConfirmationEmail(String to, String confirmationToken) {
        String subject = "Підтвердження реєстрації";
        String text = "Будь ласка, підтвердіть свою адресу електронної пошти, перейшовши за посиланням: " + confirmationToken;
        sendEmail(to, subject, text);
    }

    @Service
    public class TokenService {
        public String generateToken() {
            return UUID.randomUUID().toString();
        }
    }
}
