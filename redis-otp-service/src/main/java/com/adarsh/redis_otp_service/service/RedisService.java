package com.adarsh.redis_otp_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public void set(String key, Object valueObj) {

        try {
            String value = objectMapper.writeValueAsString(valueObj);
            redisTemplate.opsForValue().set(key, value);
        } catch (JsonProcessingException e) {
            log.info("Error parsing valueObj to String: {}", e.getMessage());
            throw new RuntimeException("Failed to serialize object", e);
        }

    }

    public <T> T get(String key, Class<T> targetClass) {
        try {
            String value = redisTemplate.opsForValue().get(key);
            if(Objects.isNull(value)) return null;
            return objectMapper.readValue(value, targetClass);
        } catch (Exception e) {
            log.error("Failed while parsing to {} class type: {}", targetClass, e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

}
