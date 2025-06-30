package com.yesul.alcohol.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AlcoholDetailDto {
    private Long alcoholId;
    private String alcoholName;
    private List<FoodDto> foods;
    private List<RegionDto> regions;
}