package com.yesul.community.service;

import com.yesul.common.constants.RedisConstants;
import com.yesul.community.model.entity.enums.PointType;
import com.yesul.config.RedisConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Redis 기반 활동 중복 체크 서비스
 * ex) 글쓰기, 댓글쓰기, 좋아요 등 도배 방지
 */
@Service
@RequiredArgsConstructor
public class ActivityDuplicateCheckService {

    private final RedisConfig redisConfig;

    /**
     * 최근 활동 저장
     */
    public void saveActivity(Long userId, PointType activityType, String content, int expireMinutes) {
        System.out.println("saveActivity 호출됨: userId=" + userId + ", type=" + activityType + ", content=" + content);
        RedisTemplate<String, String> redisTemplate = redisConfig.getRedisTemplate(RedisConstants.USER_POINT_DB_INDEX);
        String key = generateKey(userId, activityType, content);
        redisTemplate.opsForValue().set(key, "1", Duration.ofMinutes(expireMinutes));
    }

    /**
     * 최근 활동 중복 여부 확인
     */
    public boolean isDuplicate(Long userId, PointType activityType, String content) {
        RedisTemplate<String, String> redisTemplate = redisConfig.getRedisTemplate(RedisConstants.USER_POINT_DB_INDEX);

        String key = generateKey(userId, activityType, content);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * Redis 키 생성
     * 예: activity:123:comment_create:927362
     */
    private String generateKey(Long userId, PointType activityType, String content) {
        return "activity:" + userId + ":" + activityType + ":" + content.trim();
    }
}