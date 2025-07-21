package com.yesul.travel.service;

import java.util.List;
import java.util.stream.Collectors;

import com.yesul.exception.handler.TravelPlanNotFoundException;
import com.yesul.travel.model.dto.TravelPlanRequestDto;
import com.yesul.user.model.entity.User;
import com.yesul.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yesul.travel.model.dto.TravelPlanDto;
import com.yesul.travel.model.entity.TravelPlan;
import com.yesul.travel.repository.TravelPlanRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelPlanServiceImpl implements TravelPlanService {

    private final TravelPlanRepository travelPlanRepository;
    private final UserRepository userRepository;

    @Override
    public List<TravelPlanDto> listUserPlans(Long userId) {
        return travelPlanRepository.findAllByUserId(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TravelPlanDto getUserPlan(Long userId, Long planId) {
        TravelPlan plan = travelPlanRepository.findByIdAndUserId(planId, userId)
                .orElseThrow(() -> new TravelPlanNotFoundException(
                        "사용자 " + userId + "의 여행계획 ID=" + planId + "를 찾을 수 없습니다."));
        return toDto(plan);
    }

    @Override
    @Transactional
    public void deleteUserPlan(Long userId, Long planId) {
        TravelPlan plan = travelPlanRepository.findByIdAndUserId(planId, userId)
                .orElseThrow(() -> new TravelPlanNotFoundException(
                        "사용자 " + userId + "의 여행계획 ID=" + planId + "를 찾을 수 없습니다."));
        travelPlanRepository.delete(plan);
    }

    @Override
    public void saveTravelPlan(TravelPlanRequestDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println(userId);
        TravelPlan plan = TravelPlan.builder()
                .travelPlan(dto.getTravelPlan())
                .user(user)
                .build();

        travelPlanRepository.save(plan);
    }

    private TravelPlanDto toDto(TravelPlan tp) {
        return new TravelPlanDto(
                tp.getId(),
                tp.getUser().getId(),
                tp.getTravelPlan(),
                tp.getCreatedAt()
        );
    }
}
