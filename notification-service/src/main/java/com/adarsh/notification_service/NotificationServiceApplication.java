package com.adarsh.notification_service;

import com.adarsh.notification_service.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class NotificationServiceApplication implements CommandLineRunner {

    private final MailService mailService;

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {

        String otp = "2001";
        mailService.sendEmail("", "Test Email from Java", otp);

    }
}
