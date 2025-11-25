package com.adarsh.redis_otp_service.service;

import com.adarsh.redis_otp_service.dtos.OtpMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final RabbitTemplate rabbitTemplate;

    public boolean sendOtp(String otp, String email, String username) {
        OtpMessageDto messageDto = new OtpMessageDto();
        messageDto.setEmail(email);
        messageDto.setOtp(otp);
        messageDto.setUsername(username);

        rabbitTemplate.convertAndSend(messageDto);

        return true;
    }

}
