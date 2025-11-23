package com.adarsh.redis_otp_service.dtos;

import com.adarsh.redis_otp_service.model.Roles;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {

    private Integer id;
    private String userName;
    private String fullName;
    private String email;
    private Roles role;

}
