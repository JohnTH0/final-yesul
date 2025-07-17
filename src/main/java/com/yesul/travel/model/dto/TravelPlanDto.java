package com.yesul.travel.model.dto;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelPlanDto {

    private Long id;
    private Long userId;
    private String travelPlan;
    private LocalDateTime createdAt;
}