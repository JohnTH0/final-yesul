package com.yesul.community.service;

import com.yesul.community.model.entity.Point;
import com.yesul.community.model.entity.PointHistory;
import com.yesul.community.model.entity.enums.PointType;
import com.yesul.community.repository.PointHistoryRepository;
import com.yesul.community.repository.PointRepository;
import com.yesul.user.model.entity.User;
import com.yesul.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PointServiceImpl implements PointService {

    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final ActivityDuplicateCheckService activityDuplicateCheckService;
    private final UserRepository userRepository;

    /**
     * 포인트 적립
     */
    @Override
    @Transactional
    public void earnPoint(Long userId, PointType type, String content) {

        // 1. 부정행위(중복) 체크
        if (activityDuplicateCheckService.isDuplicate(userId, type, content)) {
            throw new IllegalArgumentException("중복된 활동으로 포인트를 적립할 수 없습니다.");
        }

        // 2. 유저 조회 & 보유 포인트 적립
        Point point = pointRepository.findByType(type)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 포인트 활동 유형입니다: " + type));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        user.earnPoint(point.getPoint());

        // 3. 포인트 적립 내역 저장
        PointHistory history = PointHistory.builder()
                .userId(userId)
                .point(point)
                .isEarned(true)
                .build();
        pointHistoryRepository.save(history);

        // 4. Redis에 활동 기록 저장 (활동 종류별 TTL)
        int ttl = switch (type) {
            case ATTENDANCE -> 1440; // 출석: 24시간 (1일 1회)
            default -> 5;             // 댓글/게시글/좋아요 등 도배 방지: 3초
        };

        activityDuplicateCheckService.saveActivity(userId, type, content, ttl);
    }


    /**
     * 포인트 차감
     */
    @Override
    @Transactional
    public void usePoint(Long userId, PointType type) {

        Point point = pointRepository.findByType(type)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 포인트 활동 유형입니다: " + type));

        // 유저 조회 & 보유 포인트 차감
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        user.usePoint(point.getPoint());

        // 포인트 차감 내역 저장
        PointHistory history = PointHistory.builder()
                .userId(userId)
                .point(point)
                .isEarned(false)
                .build();

        pointHistoryRepository.save(history);
    }

    /**
     * 포인트 내역 조회
     */
    @Override
    public List<PointHistory> getPointHistories(Long userId) {
        return pointHistoryRepository.findByUserId(userId);
    }
}