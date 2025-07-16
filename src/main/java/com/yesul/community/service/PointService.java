package com.yesul.community.service;

import com.yesul.community.model.entity.PointHistory;
import com.yesul.community.model.entity.enums.PointType;

import java.util.List;

public interface PointService {

    void earnPoint(Long userId, PointType type);

    void usePoint(Long userId, PointType type);

    List<PointHistory> getPointHistories(Long userId);

    // 활동 도배 방지용 Redis TTL 기반 중복 체크
    boolean isDuplicateActivity(Long userId, PointType type);
}