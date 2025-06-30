package com.yesul.alcohol.model.entity;

import com.yesul.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Entity
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

    @Column(length = 255)
    private String manufacturer;

    @Column(precision = 4, scale = 2, nullable = false)
    private BigDecimal abv;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "taste_description", columnDefinition = "TEXT")
    private String tasteDescription;

    @Column(nullable = false)
    private Integer price;

    @Column(length = 500)
    private String image;

    @OneToMany(mappedBy = "alcohol", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AlcoholTaste> alcoholTastes = new ArrayList<>();
}