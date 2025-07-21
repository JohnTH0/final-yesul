package com.yesul.travel.service;

import com.yesul.travel.model.dto.TravelPlanRequestDto;
import java.util.List;
import com.yesul.travel.model.dto.TravelPlanDto;


public interface TravelPlanService {
    /**
     * 로그인한 사용자가 생성한 모든 여행계획 조회
     */
    List<TravelPlanDto> listUserPlans(Long userId);

    /**
     * 로그인한 사용자의 특정 여행계획 상세 조회
     */
    TravelPlanDto getUserPlan(Long userId, Long planId);

    /**
     * 로그인한 사용자의 특정 여행계획 삭제
     */
    void deleteUserPlan(Long userId, Long planId);

    void saveTravelPlan(TravelPlanRequestDto dto, Long userId);
}