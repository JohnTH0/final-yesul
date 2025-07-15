package com.yesul.admin.service;

import com.yesul.admin.model.dto.AdminLoginLogDto;
import com.yesul.admin.model.entity.AdminLoginLog;
import com.yesul.admin.repository.AdminLoginLogRepository;
import com.yesul.alcohol.repository.AlcoholRepository;
import com.yesul.community.model.dto.PostResponseDto;
import com.yesul.community.repository.PostRepository;
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
public class AdminService {

    private final RedisTemplate<String, String> redisTemplate;
    private final AdminLoginLogRepository logRepository;
    private final PostRepository postRepository;
    private final PostRepository userRepository;
    private final AlcoholRepository alcoholRepository;
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
        List<AdminLoginLog> logs = logRepository.findAllByOrderByCreatedAtDesc();

        return logs.stream()
                .map(AdminLoginLogDto::from)
                .collect(Collectors.toList());
    }

    public List<PostResponseDto> getPopularPosts() {
        return postRepository.findPopularPostsByLikes();
    }

    //public List<PostResponseDto> getPopularAlcohol() {
    //    return AlcoholRepository.findPopularAlcoholByLikes();
    //}

    public int getUserCount() {
        return (int) userRepository.count();
    }

    public int getAlcoholCount() {
        return (int) alcoholRepository.count();
    }
}
