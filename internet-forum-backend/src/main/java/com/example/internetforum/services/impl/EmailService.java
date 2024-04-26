package com.example.internetforum.services.impl;

import com.example.internetforum.services.EmailServiceInterface;
import jakarta.transaction.Transactional;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EmailService implements EmailServiceInterface {

    private final JavaMailSender javaMailSender;
    private final SiemService siemService;

    public EmailService(JavaMailSender javaMailSender, SiemService siemService) {
        this.javaMailSender = javaMailSender;
        this.siemService = siemService;
    }



    public void sendUserApprovedMessage(String email) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Account approved notification");
            message.setText("Your account has been approved!");
            this.javaMailSender.send(message);
        }catch (Exception e){
            throw new RuntimeException("User approved email can not be sent!");
        }
    }

    public void sendVerificationCodeToUser(String email, String verificationCode) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Login verification code");
            message.setText("Your login verification code: " + verificationCode);
            this.javaMailSender.send(message);
        }catch (Exception e){
            throw new RuntimeException("Could not sent verification code to user!");
        }
    }
}
