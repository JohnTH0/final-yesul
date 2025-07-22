package com.yesul.community.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_image")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter // 반드시 있어야 함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "image_url", columnDefinition = "TEXT", nullable = false)
    private String imageUrl;
}