package com.adarsh.redis_otp_service.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OtpResponseDto {

    private String userName;
    private String message;
    private LocalDateTime currentTime;
    private Integer expiresIn;
    private String otp;

}
