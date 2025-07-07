package com.yesul.chatroom.service;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TempMessageServiceImpl implements TempMessageService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final long TTL_SECONDS = 60 * 60; // 1시간

    @Override
    public void saveTempMessage(Long chatRoomId, Long userId, String message) {
        redisTemplate.opsForValue().set(key(chatRoomId, userId), message, TTL_SECONDS, TimeUnit.SECONDS);
    }

    @Override
    public String getTempMessage(Long chatRoomId, Long userId) {
        return redisTemplate.opsForValue().get(key(chatRoomId, userId));
    }

    @Override
    public void deleteTempMessage(Long chatRoomId, Long userId) {
        redisTemplate.delete(key(chatRoomId, userId));
    }

    private String key(Long chatRoomId, Long userId) {
        return "temp-message:" + chatRoomId + ":" + userId;
    }
}
