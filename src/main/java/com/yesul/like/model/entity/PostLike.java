package com.yesul.like.model.entity;

import jakarta.persistence.*;

import lombok.*;

import com.yesul.common.BaseTimeEntity;
import com.yesul.community.model.entity.Post;
import com.yesul.user.model.entity.User;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
@Entity
@Table(name = "post_like")
public class PostLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @ToString.Exclude
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

}

