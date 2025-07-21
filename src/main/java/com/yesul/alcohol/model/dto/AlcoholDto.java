package com.yesul.alcohol.model.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlcoholDto {

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
    private Integer likeCount;

    private Integer price;
    private String pairingFoods;
    private String image;

    public AlcoholDto(Long id, String name, String brand, String province,
                      String city, String type, Integer volumeMl, BigDecimal abv, String image) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.province = province;
        this.city = city;
        this.type = type;
        this.volumeMl = volumeMl;
        this.abv = abv;
        this.image = image;
    }

    public AlcoholDto(Long id, String name, String province, String city, Integer likeCount) {
        this.id = id;
        this.name = name;
        this.province = province;
        this.city = city;
        this.likeCount = likeCount;
    }


}