package com.yesul.community.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_image")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 게시글의 이미지인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;
}