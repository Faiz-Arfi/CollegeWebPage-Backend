package com.example.utkarshbackend.services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendVerificationEmail(String toEmail, String verificationToken) {
        String subject = "Email Verification";
        String body = "Click the button below to verify your email:";
        String path = "/verify-email";
        sendEmail(toEmail, subject, body, path, verificationToken);
    }

    public void sendResetPasswordEmail(String toEmail, String resetToken) {
        String subject = "Reset Password";
        String body = "Click the button below to reset your password:";
        String path = "/reset-password";
        sendEmail(toEmail, subject, body, path, resetToken);
    }

    public void sendContactUsEmail(String toEmail, String subject, String body) {
        sendContactEmail(toEmail, subject, body);
    }

    private void sendContactEmail(String toEmail, String subject, String body) {
        try {
            String content = """
                <div style="font-family: Arial, sans-serif; max-width: 650px; margin: auto; padding: 25px; 
                            border: 1px solid #dcdcdc; border-radius: 8px; background-color: #ffffff;">
                    
                    <div style="text-align: center; padding-bottom: 15px; border-bottom: 2px solid #004080;">
                        <h2 style="color: #004080; margin: 0;">%s</h2>
                    </div>
                    
                    <div style="padding: 20px 0; color: #333333; font-size: 15px; line-height: 1.6;">
                        %s
                    </div>
                    
                    <div style="margin-top: 20px; padding: 15px; background-color: #f5f8fc; 
                                border-left: 4px solid #004080; border-radius: 4px; font-size: 14px; color: #333;">
                        <p style="margin: 0;">Thank you for contacting us. Our team will get back to you shortly. 
                        If your inquiry is urgent, please reach out to the administration office directly.</p>
                    </div>
                    <div style="text-align: center; margin-top: 25px; font-size: 13px; color: #777;">
                        <p style="margin: 0;">&copy; %d UCET, VBU, Hazaribag. All rights reserved.</p>
                    </div>
                </div>
            """.formatted(subject, body, java.time.Year.now().getValue());
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setFrom(fromEmail);
            helper.setText(content, true);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private void sendEmail(String toEmail, String subject, String body, String path, String token) {
        try {
            String actionUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(path)
                    .queryParam("token", token)
                    .toUriString();
            String content = """
                    <div style="font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ccc; border-radius: 5px;">
                        <h2 style="color: #333;">%s</h2>
                        <p style="color: #555;">%s</p>
                        <a href="%s" style="display: inline-block; padding: 10px 20px; background-color: #28a745; color: white; text-decoration: none; border-radius: 5px;">Click Here</a>
                        <p style="color: #555;">This Link is only valid for 10min</p>
                        <p style="color: #555;">Or copy and paste this link into your browser:</p>
                        <p style="color: #007bff; word-break: break-all;">%s</p>
                        <p style="color: #555;">Thank you.</p>
                        <p style="color: #777;">If you did not request this, please ignore this email.</p>
                    </div>
                    """.formatted(subject, body, actionUrl, actionUrl);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setFrom(fromEmail);
            helper.setText(content, true);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
