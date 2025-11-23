package com.adarsh.redis_otp_service.dtos;

import lombok.Data;

@Data
public class LoginRequestDto {

    private String userName;
    private String password;

}
