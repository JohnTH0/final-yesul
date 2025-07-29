package com.yesul.common.filter;

import com.yesul.config.RedisConfig;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.UUID;

import static com.yesul.common.constants.RedisConstants.SYSTEM_MONITORING_DB_INDEX;

@RequiredArgsConstructor
@Component
public class SystemMonitoringFilter implements Filter {

    private final RedisConfig redisConfig;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String visitorId = getOrCreateVisitorId(req, res); //UUID 쿠키 발급 및 조회

        //Redis에 저장하기 (Set)
        RedisTemplate<String, String> redisTemplate = redisConfig.getRedisTemplate(SYSTEM_MONITORING_DB_INDEX);

        String todayKey = "visitors:" + LocalDate.now();
        redisTemplate.opsForSet().add(todayKey, visitorId);

        if (Boolean.TRUE.equals(redisTemplate.getExpire(todayKey) == -1)) { // 키의 남은 TTL이 -1이라면 (TTL 설정 전, 오늘 첫방문을 의미)
            redisTemplate.expire(todayKey, Duration.ofDays(2)); // 만료기간설정
        }

        String activeKey = "online-users:" + visitorId;
        redisTemplate.opsForValue().set(activeKey, "active");
        redisTemplate.expire(activeKey, Duration.ofMinutes(5));

        chain.doFilter(request, response);
    }

    private String getOrCreateVisitorId(HttpServletRequest req, HttpServletResponse res) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("visitorId".equals(c.getName())) {
                    return c.getValue();
                }
            }
        }
        String uuid = UUID.randomUUID().toString();
        Cookie newCookie = new Cookie("visitorId", uuid);
        newCookie.setMaxAge(60 * 60 * 24 * 365); // 1년
        newCookie.setPath("/");
        res.addCookie(newCookie);
        return uuid;
    }
}