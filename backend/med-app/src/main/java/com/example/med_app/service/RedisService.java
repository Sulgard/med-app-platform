package com.example.med_app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class RedisService {

    private final RedisCacheManager cacheManager;

//    public void setValue(String key, String value) {
//        redisTemplate.opsForValue().set(key, value);
//    }
//
//    public void setValueWithExpiry(String key, String value, long seconds) {
//        redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(seconds));
//    }
//
//    public String getValue(String key) {
//        return redisTemplate.opsForValue().get(key);
//    }
//
//    public boolean exists(String key) {
//        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
//    }




}
