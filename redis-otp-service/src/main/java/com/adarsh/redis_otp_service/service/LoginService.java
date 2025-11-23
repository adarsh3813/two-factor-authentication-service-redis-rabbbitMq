package com.adarsh.redis_otp_service.service;

import com.adarsh.redis_otp_service.dtos.*;
import com.adarsh.redis_otp_service.model.Roles;
import com.adarsh.redis_otp_service.model.User;
import com.adarsh.redis_otp_service.repository.UserRepository;
import com.adarsh.redis_otp_service.security.JwtUtil;
import com.adarsh.redis_otp_service.security.UserDetailsImpl;
import com.adarsh.redis_otp_service.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final RedisService redisService;
    private static final Integer OTP_LENGTH = 4;
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public LoginResultResponse signUpUser(SignUpRequestDto dto) {
        if(userRepository.findByUserName(dto.getUserName()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setRole(Roles.CUSTOMER);
        user.setPassword(dto.getPassword());
        user.setFullName(dto.getFullName());

        User savedUser = userRepository.save(user);
        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getUserName());
        String token = jwtUtil.generateToken(userDetails);

        return LoginResultResponse.builder()
                .userName(dto.getUserName())
                .message("User verified please proceed to verify using OTP sent to your email ID")
                .token(token)
                .build();
    }

    public LoginResultResponse handleUserLogin(LoginRequestDto dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUserName(), dto.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getUserName());
        String token = jwtUtil.generateToken(userDetails);

        return LoginResultResponse.builder()
                .userName(dto.getUserName())
                .message("User verified please proceed to verify using OTP sent to your email ID")
                .token(token)
                .build();
    }

    public OtpResponseDto generateOtp(LoginRequestDto dto) {

        String otp = generateOtp();
        redisService.set(dto.getUserName(), otp);

        return OtpResponseDto.builder().
                userName(dto.getUserName())
                .oneTimePassword(otp)
                .expiryTime(LocalDate.now().plus(30, ChronoUnit.SECONDS))
                .build();
    }

    public LoginResultResponse validateOtp(OtpSubmitRequestDto dto) {
        String correctOtp = redisService.get(dto.getUserName(), String.class);

        if(Objects.isNull(correctOtp)) {
            return LoginResultResponse.builder()
                    .userName(dto.getUserName())
                    .message("Invalid request. Please generate an otp for this user")
                    .build();
        }

        if(dto.getOtp().equals(correctOtp)) {
            return LoginResultResponse.builder()
                    .userName(dto.getUserName())
                    .message("Login successful!")
                    .token("RANDOM_TEMPORARY_TOKEN")
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
