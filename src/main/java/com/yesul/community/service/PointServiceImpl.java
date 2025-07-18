package com.yesul.community.service;

import com.yesul.community.model.entity.Point;
import com.yesul.community.model.entity.PointHistory;
import com.yesul.community.model.entity.enums.PointType;
import com.yesul.community.repository.PointHistoryRepository;
import com.yesul.community.repository.PointRepository;
import com.yesul.exception.handler.PointNotFoundException;
import com.yesul.exception.handler.UserNotFoundException;
import com.yesul.user.model.entity.User;
import com.yesul.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PointServiceImpl implements PointService {

    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final ActivityDuplicateCheckService activityDuplicateCheckService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void earnPoint(Long userId, PointType type) {
        if (activityDuplicateCheckService.isDuplicate(userId, type)) {
            throw new IllegalArgumentException("중복된 활동으로 포인트를 적립할 수 없습니다.");
        }

        Point point = pointRepository.findByType(type)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 포인트 활동 유형입니다: " + type));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        user.earnPoint(point.getPoint());

        PointHistory history = PointHistory.builder()
                .userId(userId)
                .point(point)
                .isEarned(true)
                .build();
        pointHistoryRepository.save(history);

        int ttlSeconds = switch (type) {
            case ATTENDANCE -> 86400;
            default -> 20;
        };

        activityDuplicateCheckService.saveActivity(userId, type, ttlSeconds);
    }

    @Override
    public boolean isDuplicateActivity(Long userId, PointType type) {
        return activityDuplicateCheckService.isDuplicate(userId, type);
    }

    @Override
    @Transactional
    public void usePoint(Long userId, PointType type) {
        Point point = pointRepository.findByType(type)
                .orElseThrow(() -> new PointNotFoundException("잘못된 포인트 활동 유형입니다: " + type));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저가 존재하지 않습니다."));
        user.usePoint(point.getPoint());

        PointHistory history = PointHistory.builder()
                .userId(userId)
                .point(point)
                .isEarned(false)
                .build();

        pointHistoryRepository.save(history);
    }

    @Override
    public List<PointHistory> getPointHistories(Long userId) {
        return pointHistoryRepository.findByUserId(userId);
    }
}