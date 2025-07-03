package com.yesul.alcohol.model.entity;

import com.yesul.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Table(name = "alcohol")
@NoArgsConstructor
public class Alcohol extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 255)
    private String brand;

    @Column(nullable = false, length = 50)
    private String province;

    @Column(nullable = false, length = 50)
    private String city;

    @Column(nullable = false, length = 255)
    private String type;

    @Column(name = "volume_ml", nullable = false)
    private Integer volumeMl;

    @Column(precision = 4, scale = 2, nullable = false)
    private BigDecimal abv;

    @Column(name = "ingredients", length = 1024)
    private String ingredients;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "taste_description", columnDefinition = "TEXT")
    private String tasteDescription;

    // 단맛
    @Column(name = "sweetness_level", nullable = true)
    private Integer sweetnessLevel;

    // 산미
    @Column(name = "acidity_level", nullable = true)
    private Integer acidityLevel;

    // 바디
    @Column(name = "body_level", nullable = true)
    private Integer bodyLevel;

    // 향
    @Column(name = "aroma_level", nullable = true)
    private Integer aromaLevel;

    // 탄닌
    @Column(name = "tannin_level", nullable = true)
    private Integer tanninLevel;

    // 끝맛
    @Column(name = "finish_level", nullable = true)
    private Integer finishLevel;

    // 탄산
    @Column(name = "sparkling_level", nullable = true)
    private Integer sparklingLevel;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "pairing_foods", length = 255)
    private String pairingFoods; // 예: "흑돼지, 전복구이, 옥돔구이"

    @Column(length = 500)
    private String image;
}