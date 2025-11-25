package com.adarsh.redis_otp_service.dtos;

import lombok.Data;

@Data
public class OtpMessageDto {
    private String otp;
    private String email;
    private String username;
}
