package com.yesul.community.model.entity;

import com.yesul.common.BaseTimeEntity;
import com.yesul.user.model.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder

@Entity(name = "Post")
@Table(name = "post")

public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "board_name", length = 40, nullable = false)
    private String boardName;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0;

    @Column(name = "thumbnail")
    private String thumbnail;
}
