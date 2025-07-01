package com.yesul.alcohol.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "region")
@NoArgsConstructor
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String province;

    @Column(nullable = false, length = 100)
    private String city;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AlcoholRegion> alcoholRegions = new ArrayList<>();


    public void addAlcohol(Alcohol alcohol) {
        AlcoholRegion ar = new AlcoholRegion();
        ar.setRegion(this);
        ar.setAlcohol(alcohol);

        this.alcoholRegions.add(ar);
        alcohol.getAlcoholRegions().add(ar);
    }
}