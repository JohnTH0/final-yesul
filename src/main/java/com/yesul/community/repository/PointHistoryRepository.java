package com.yesul.community.repository;

import com.yesul.community.model.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    // 유저의 포인트 내역 조회
    List<PointHistory> findByUserId(Long userId);
}