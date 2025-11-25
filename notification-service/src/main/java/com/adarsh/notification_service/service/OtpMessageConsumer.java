package com.adarsh.notification_service.service;

import com.adarsh.notification_service.dto.OtpMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpMessageConsumer {

    private final MailService mailService;
    private static final String OTP_MAIL_SUBJECT = "One Time Password to Login";

    @RabbitListener(queues = "otpMessageQueue")
    public void consumeOtp(OtpMessageDto otpMessageDto) {

        mailService.sendEmail(otpMessageDto.getEmail(), OTP_MAIL_SUBJECT, otpMessageDto.getUsername(), otpMessageDto.getOtp());

    }

}
