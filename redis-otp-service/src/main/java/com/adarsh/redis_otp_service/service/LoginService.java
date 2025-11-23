package com.adarsh.redis_otp_service.service;

import com.adarsh.redis_otp_service.dtos.*;
import com.adarsh.redis_otp_service.model.Roles;
import com.adarsh.redis_otp_service.model.User;
import com.adarsh.redis_otp_service.repository.UserRepository;
import com.adarsh.redis_otp_service.security.JwtUtil;
import com.adarsh.redis_otp_service.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LoginService {

    private static final Integer OTP_LENGTH = 4;
    private static final Integer OTP_TIMEOUT = 2;

    private final UserService userService;
    private final RedisService redisService;
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public OtpResponseDto signUpUser(SignUpRequestDto dto) {
        if(userRepository.findByUserName(dto.getUserName()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        userService.saveNewUser(dto);

        String otp = generateOtp();
        redisService.set("otp:"+dto.getUserName(), otp, OTP_TIMEOUT);

        return OtpResponseDto.builder()
                .userName(dto.getUserName())
                .message("Account created! OTP has been sent to the linked emailId")
                .currentTime(LocalDateTime.now())
                .expiresIn(OTP_TIMEOUT)
                .otp(otp)
                .build();
    }

    public OtpResponseDto handleUserLogin(LoginRequestDto dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUserName(), dto.getPassword())
        );

        String otp = generateOtp();
        redisService.set("otp:"+dto.getUserName(), otp, OTP_TIMEOUT);

        return OtpResponseDto.builder()
                .userName(dto.getUserName())
                .message("OTP has been sent to the linked emailId")
                .currentTime(LocalDateTime.now())
                .expiresIn(OTP_TIMEOUT)
                .otp(otp)
                .build();
    }

    public LoginResultResponse validateOtp(OtpSubmitRequestDto dto) {
        String correctOtp = redisService.get("otp:"+dto.getUserName());

        if(Objects.isNull(correctOtp)) {
            return LoginResultResponse.builder()
                    .userName(dto.getUserName())
                    .message("Invalid request. Please generate an otp for this user")
                    .build();
        }

        if(dto.getOtp().equals(correctOtp)) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getUserName());
            String token = jwtUtil.generateToken(userDetails);

            redisService.delete("otp:"+dto.getUserName());

            return LoginResultResponse.builder()
                    .userName(dto.getUserName())
                    .message("Login successful!")
                    .token(token)
                    .build();
        } else {
            return LoginResultResponse.builder()
                    .userName(dto.getUserName())
                    .message("OTP invalid! Please retry.")
                    .build();
        }
    }

    private String generateOtp() {

        SecureRandom secureRandom = new SecureRandom();
        StringBuilder otp = new StringBuilder(OTP_LENGTH);

        for(int i = 0; i < OTP_LENGTH; i++) {
            otp.append(secureRandom.nextInt(10));
        }

        return otp.toString();
    }

}
