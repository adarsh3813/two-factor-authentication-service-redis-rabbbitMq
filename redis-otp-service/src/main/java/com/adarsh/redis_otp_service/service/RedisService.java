package com.adarsh.redis_otp_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public void set(String key, String value, Integer ttl) {
        redisTemplate.opsForValue().set(key, value, ttl, TimeUnit.MINUTES);
    }

    public String get(String key) {
        try {
            String value = redisTemplate.opsForValue().get(key);
            if(Objects.isNull(value)) return null;
            return value;
        } catch (Exception e) {
            log.error("Failed while parsing to {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

}
