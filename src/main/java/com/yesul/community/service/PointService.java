package com.yesul.community.service;

import com.yesul.community.model.entity.PointHistory;
import com.yesul.community.model.entity.enums.PointType;

import java.util.List;

public interface PointService {

    void earnPoint(Long userId, PointType type, String content);

    void usePoint(Long userId, PointType type);

    List<PointHistory> getPointHistories(Long userId);
}