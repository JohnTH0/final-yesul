package com.yesul.alcohol.model.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "taste")
@NoArgsConstructor
public class Taste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String description;

    @OneToMany(mappedBy = "taste", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AlcoholTaste> alcoholTastes = new ArrayList<>();
}