package com.adarsh.redis_otp_service.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ErrorResponse {
    private String message;
    private LocalDate timeStamp;
}
