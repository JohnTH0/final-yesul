package com.yesul.travel.service;

import com.yesul.travel.model.dto.TravelPlanRequestDto;
import com.yesul.travel.model.dto.TravelPlanDto;
import com.yesul.travel.model.entity.TravelPlan;
import com.yesul.travel.repository.TravelPlanRepository;
import com.yesul.user.model.entity.User;
import com.yesul.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TravelPlanService {

    private final TravelPlanRepository travelPlanRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public TravelPlanDto getTravelPlan(int TravelPlanId) {
        return null;
    }

    public TravelPlan saveTravelPlan(TravelPlanRequestDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println(userId);
        TravelPlan plan = TravelPlan.builder()
                .travelPlan(dto.getTravelPlan())
                .user(user)
                .build();
        System.out.println(plan);

         travelPlanRepository.save(plan);

        System.out.println(plan);

        return plan;
    }
}