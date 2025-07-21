package com.yesul.travel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yesul.travel.model.entity.TravelPlan;

public interface TravelPlanRepository extends JpaRepository<TravelPlan, Long> {

    /**
     * 해당 사용자가 생성한 모든 여행계획 조회
     */
    List<TravelPlan> findAllByUserId(Long userId);

    /**
     * 해당 사용자의 특정 여행계획 조회
     */
    Optional<TravelPlan> findByIdAndUserId(Long id, Long userId);
}
