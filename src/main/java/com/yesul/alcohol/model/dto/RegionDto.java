package com.yesul.alcohol.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegionDto {
    private Long id;
    private String province;
    private String city;
}