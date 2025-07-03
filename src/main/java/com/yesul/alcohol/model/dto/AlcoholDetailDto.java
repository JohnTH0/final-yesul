package com.yesul.alcohol.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlcoholDetailDto {
    private Long id;
    private String province;
    private String city;
    private String name;
    private String brand;
    private String type;
    private Integer volumeMl;
    private BigDecimal abv;
    private String ingredients;
    private String description;
    private String tasteDescription;

    private Integer sweetnessLevel;
    private Integer acidityLevel;
    private Integer bodyLevel;
    private Integer aromaLevel;
    private Integer tanninLevel;
    private Integer finishLevel;
    private Integer sparklingLevel;

    private Integer price;
    private String pairingFoods;
    private String image;
}