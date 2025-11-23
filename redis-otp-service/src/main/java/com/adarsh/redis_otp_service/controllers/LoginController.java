package com.adarsh.redis_otp_service.controllers;

import com.adarsh.redis_otp_service.dtos.*;
import com.adarsh.redis_otp_service.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<Object> login(@RequestBody LoginRequestDto dto) {

        LoginResultResponse loginResultResponse = loginService.handleUserLogin(dto);
        return ResponseEntity.ok().body(loginResultResponse);
    }

    @PostMapping("/validateOtp")
    public ResponseEntity<Object> validateOtp(@RequestBody OtpSubmitRequestDto dto, Authentication authentication) {

        if(dto.getUserName().equals(authentication.getName())) {
            LoginResultResponse response = loginService.validateOtp(dto);
            return ResponseEntity.ok().body(response);
        }
        return new ResponseEntity<>(ErrorResponse.builder().
                timeStamp(LocalDate.now()).
                message("Invalid user")
                .build(), HttpStatus.FORBIDDEN);
    }

    @PostMapping("/signUp")
    public ResponseEntity<Object> signUpUser(@RequestBody SignUpRequestDto dto) {
        LoginResultResponse loginResultResponse = loginService.signUpUser(dto);
        return ResponseEntity.ok().body(loginResultResponse);
    }

}
