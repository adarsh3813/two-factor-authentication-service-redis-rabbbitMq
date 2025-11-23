package com.adarsh.redis_otp_service.controllers;

import com.adarsh.redis_otp_service.dtos.*;
import com.adarsh.redis_otp_service.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<Object> login(@RequestBody LoginRequestDto dto) {

        OtpResponseDto otpResponseDto = loginService.handleUserLogin(dto);
        return ResponseEntity.ok().body(otpResponseDto);
    }

    @PostMapping("/validateOtp")
    public ResponseEntity<Object> validateOtp(@RequestBody OtpSubmitRequestDto dto) {

        try {
            LoginResultResponse response = loginService.validateOtp(dto);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorResponse.builder().
                    timeStamp(LocalDateTime.now()).
                    message("Invalid Otp")
                    .build(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signUp")
    public ResponseEntity<Object> signUpUser(@RequestBody SignUpRequestDto dto) {
        OtpResponseDto otpResponseDto = loginService.signUpUser(dto);
        return ResponseEntity.ok().body(otpResponseDto);
    }

}
