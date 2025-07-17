package com.yesul.travel.service;

import com.yesul.travel.model.dto.TravelPlanDto;
import com.yesul.travel.repository.TravelPlanRepository;
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
    private final ModelMapper modelMapper;

    public TravelPlanDto getTravelPlan(int TravelPlanId) {
        return null;
    }


}