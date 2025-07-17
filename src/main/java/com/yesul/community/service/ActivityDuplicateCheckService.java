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
     * @param userId 유저 ID
     * @param activityType 활동 종류
     * @param expireSeconds 제한 시간(초)
     */
    public void saveActivity(Long userId, PointType activityType, int expireSeconds) {
        RedisTemplate<String, String> redisTemplate = redisConfig.getRedisTemplate(RedisConstants.USER_POINT_DB_INDEX);
        String key = generateKey(userId, activityType);
        System.out.println("💾 Redis 저장 시도! key = " + key + ", expire = " + expireSeconds + "초");
        redisTemplate.opsForValue().set(key, "1", Duration.ofSeconds(expireSeconds));
    }

    /**
     * 최근 활동 중복 여부 확인
     * @param userId 유저 ID
     * @param activityType 활동 종류
     * @return true = 중복(도배), false = 정상
     */
    public boolean isDuplicate(Long userId, PointType activityType) {
        RedisTemplate<String, String> redisTemplate = redisConfig.getRedisTemplate(RedisConstants.USER_POINT_DB_INDEX);
        String key = generateKey(userId, activityType);
        Boolean exists = redisTemplate.hasKey(key);
        return Boolean.TRUE.equals(exists);
    }

    /**
     * Redis 키 생성
     * 예: activity:123:POST_CREATE
     */
    public String generateKey(Long userId, PointType activityType) {
        return "activity:" + userId + ":" + activityType;
    }
}