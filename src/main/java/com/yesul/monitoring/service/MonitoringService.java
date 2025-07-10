package com.yesul.monitoring.service;

import com.yesul.login.model.dto.AdminLoginLogDto;
import com.yesul.login.model.entity.AdminLoginLog;
import com.yesul.monitoring.repository.AdminLoginLogRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MonitoringService {

    private final RedisTemplate<String, String> redisTemplate;
    private final AdminLoginLogRepository logRepository;
    private final ModelMapper modelMapper;

    public int getTodayVisitorCount() {
        String todayKey = "visitors:" + LocalDate.now();
        Long count = redisTemplate.opsForSet().size(todayKey);
        return count != null ? count.intValue() : 0;
    }

    public int getRealTimeUserCount() {
        ScanOptions options = ScanOptions.scanOptions() //SCAN 설정
                .match("online-users:*")
                .count(1000)
                .build();

        Cursor<byte[]> cursor = (Cursor<byte[]>) redisTemplate.executeWithStickyConnection(
                connection -> connection.scan(options)
        );

        int count = 0;
        while (cursor != null && cursor.hasNext()) {
            cursor.next();
            count++;
        }

        return count;
    }

    public void logAdminLogin(String ip, String userAgent) {
        AdminLoginLog log = AdminLoginLog.builder()
                .ip(ip)
                .userAgent(userAgent)
                .build();

        logRepository.save(log);
    }

    public List<AdminLoginLogDto> getAllLoginLogs() {
        List<AdminLoginLog> logs = logRepository.findAll();

        return logs.stream().map(log -> modelMapper.map(log, AdminLoginLogDto.class)).collect(Collectors.toList());
    }
}
