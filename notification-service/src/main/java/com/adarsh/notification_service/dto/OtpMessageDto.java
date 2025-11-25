package com.adarsh.notification_service.dto;

import lombok.Data;

@Data
public class OtpMessageDto {
    private String otp;
    private String email;
    private String username;
}