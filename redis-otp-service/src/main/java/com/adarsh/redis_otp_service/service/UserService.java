package com.adarsh.redis_otp_service.service;

import com.adarsh.redis_otp_service.dtos.SignUpRequestDto;
import com.adarsh.redis_otp_service.dtos.UserResponseDto;
import com.adarsh.redis_otp_service.model.Roles;
import com.adarsh.redis_otp_service.model.User;
import com.adarsh.redis_otp_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDto getUser(String userName) {

        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("No user found"));

        return UserResponseDto.builder()
                .email(user.getEmail())
                .id(user.getId())
                .role(user.getRole())
                .fullName(user.getFullName())
                .userName(user.getUserName())
                .build();
    }

    public void saveNewUser(SignUpRequestDto dto) {
        User user = new User();
        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setRole(Roles.CUSTOMER);
        user.setPassword(dto.getPassword());
        user.setFullName(dto.getFullName());

        userRepository.save(user);
    }

}
