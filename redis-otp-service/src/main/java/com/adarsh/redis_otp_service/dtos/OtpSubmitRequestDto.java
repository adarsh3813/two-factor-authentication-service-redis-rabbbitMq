package com.adarsh.redis_otp_service.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OtpSubmitRequestDto {

    private String userName;
    private String otp;
    private LocalDate currentTime;

}
