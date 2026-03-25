package com.financetrackerapp.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("Verify Your Email - Life OS");

            String html = String.format("""
                <div style="font-family: Arial, sans-serif; background-color:#f4f6f7; padding:30px;">
                    <div style="max-width:600px; margin:auto; background:white; padding:20px; border-radius:10px; text-align:center;">
                        
                        <h2 style="color:#2E86C1;">Welcome to Life OS 🚀</h2>
                        
                        <p style="font-size:16px; color:#555;">
                            Where you manage your <b>finance</b>, <b>health</b>, and <b>daily activities</b>.
                        </p>
                        
                        <hr style="margin:20px 0;">
                        
                        <p style="font-size:16px;">Your OTP for verification is:</p>
                        
                        <h1 style="color:#28B463; letter-spacing:3px;">%s</h1>
                        
                        <p style="color:#888;">Valid for 5 minutes</p>
                        
                        <br>
                        
                        <p style="font-size:12px; color:#aaa;">
                            If you did not request this, please ignore this email.
                        </p>
                    </div>
                </div>
                """, otp);

            helper.setText(html, true);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Email sending failed");
        }
    }
}
