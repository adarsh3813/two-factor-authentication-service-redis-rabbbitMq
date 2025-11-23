package com.adarsh.redis_otp_service.controllers;

import com.adarsh.redis_otp_service.dtos.UserResponseDto;
import com.adarsh.redis_otp_service.service.UserContextService;
import com.adarsh.redis_otp_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserContextService userContextService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Object> getUser() {

        String userName = userContextService.getCurrentUsername();
        UserResponseDto responseDto = userService.getUser(userName);
        return ResponseEntity.ok().body(responseDto);
    }
}
