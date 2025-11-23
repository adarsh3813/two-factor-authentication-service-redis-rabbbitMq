package com.adarsh.redis_otp_service.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class OtpResponseDto {

    private String userName;
    private String oneTimePassword;
    private LocalDate expiryTime;

}
