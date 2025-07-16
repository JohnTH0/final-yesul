package com.yesul.like.model.entity;

import jakarta.persistence.*;
import lombok.*;
import com.yesul.common.BaseTimeEntity;
import com.yesul.user.model.entity.User;
import com.yesul.alcohol.model.entity.Alcohol;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "alcohol_like")
public class AlcoholLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alcohol_id")
    private Alcohol alcohol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
