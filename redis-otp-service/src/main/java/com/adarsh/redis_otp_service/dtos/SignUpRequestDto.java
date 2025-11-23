package com.adarsh.redis_otp_service.dtos;

import com.adarsh.redis_otp_service.model.Roles;
import lombok.Data;

@Data
public class SignUpRequestDto {

    private String userName;
    private String fullName;
    private String password;
    private String email;
    private Roles role;

}
