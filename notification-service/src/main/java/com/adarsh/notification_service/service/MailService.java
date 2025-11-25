package com.adarsh.notification_service.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;


    public void sendEmail(String to, String subject, String username, String otp) {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);

            try (var inputStream = Objects.requireNonNull(MailService.class.getResourceAsStream("/templates/otp-email-template.html"))) {
                String html = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                html = html.replace("{{OTP}}", otp);
                helper.setText(html,true);
            }

            javaMailSender.send(message);

        } catch (Exception e) {
            log.error("Exception occurred: {}", e);
        }
    }

}
