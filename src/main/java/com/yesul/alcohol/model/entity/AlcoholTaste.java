package com.yesul.alcohol.model.entity;

import com.yesul.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "alcohol_taste")
@NoArgsConstructor
public class AlcoholTaste {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "alcohol_id", nullable = false)
        private Alcohol alcohol;

        @ManyToOne
        @JoinColumn(name = "alcohol_taste_id", nullable = false)
        private Taste taste;

        @Column()
        private Integer level;
    }