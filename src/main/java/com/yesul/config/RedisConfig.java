package com.yesul.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    /**
     * redis의 연결 정보 설정
     */

    // RedisTemplate 캐싱 (DB 인덱스별로 하나씩만 생성해서 재사용)
    private final Map<Integer, RedisTemplate<String, String>> redisTemplateMap = new ConcurrentHashMap<>();

    public RedisTemplate<String, String> getRedisTemplate(int dbIndex) {
        return redisTemplateMap.computeIfAbsent(dbIndex, this::createRedisTemplateForDb);
    }

    private RedisTemplate<String, String> createRedisTemplateForDb(int dbIndex) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);
        config.setDatabase(dbIndex);

        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(config);
        connectionFactory.afterPropertiesSet();

        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

}
