package com.yesul.user.model.entity;

import jakarta.persistence.*;
import lombok.*;

import com.yesul.alcohol.model.entity.Alcohol;
import com.yesul.common.BaseTimeEntity;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "user_alcohol")
public class FavoriteAlcohol extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alcohol_id")
    @ToString.Exclude
    private Alcohol alcohol;

}
