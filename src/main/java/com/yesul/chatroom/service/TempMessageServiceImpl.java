package com.yesul.chatroom.service;

import com.yesul.config.RedisConfig;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TempMessageServiceImpl implements TempMessageService {

    private final RedisConfig redisConfig;
    private static final long TTL_SECONDS = 60 * 60; // 1시간

    @Override
    @Transactional
    public void saveTempMessage(Long chatRoomId, Long userId, String message, int dbIndex) {
        RedisTemplate<String, String> redisTemplate = redisConfig.getRedisTemplate(dbIndex);  // DB별로 RedisTemplate 가져오기
        redisTemplate.opsForValue().set(key(chatRoomId, userId), message, TTL_SECONDS, TimeUnit.SECONDS);
    }

    @Override
    public String getTempMessage(Long chatRoomId, Long userId, int dbIndex) {
        RedisTemplate<String, String> redisTemplate = redisConfig.getRedisTemplate(dbIndex);
        return redisTemplate.opsForValue().get(key(chatRoomId, userId));
    }

    @Override
    public void deleteTempMessage(Long chatRoomId, Long userId, int dbIndex) {
        RedisTemplate<String, String> redisTemplate = redisConfig.getRedisTemplate(dbIndex);
        redisTemplate.delete(key(chatRoomId, userId));
    }

    private String key(Long chatRoomId, Long userId) {
        return "temp-message:" + chatRoomId + ":" + userId;
    }
}
