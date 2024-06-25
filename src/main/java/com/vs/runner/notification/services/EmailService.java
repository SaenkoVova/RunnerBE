package com.vs.runner.notification.services;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final ResourceLoader resourceLoader;

    public Boolean sendRegistrationConfirmationEmail(String to, String token) {
        try {
            Resource resource = resourceLoader.getResource("classpath:static/registration_confirmation.html");
            String htmlTemplate = new String(resource.getInputStream().readAllBytes());
            htmlTemplate = htmlTemplate.replace("${token}", token);
            return sendHtmlEmail(to, "Registration confirmation", htmlTemplate);
        } catch (Exception e) {
            System.out.println("Email sending error: " + e.getMessage());
            return false;
        }
    }

    public Boolean sendHtmlEmail(String to, String subject, String htmlTemplate) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            message.setFrom(new InternetAddress("vova080520@gmail.com"));
            message.setRecipients(MimeMessage.RecipientType.TO, to);
            message.setSubject(subject);
            message.setContent(htmlTemplate, "text/html; charset=utf-8");

            mailSender.send(message);
            return true;
        } catch (Exception e) {
            System.out.println("Email sending error: " + e.getMessage());
            return false;
        }
    }
}
