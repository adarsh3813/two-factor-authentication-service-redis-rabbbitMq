package com.adarsh.redis_otp_service.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResultResponse {

    private String userName;
    private String message;
    private String token;

}
