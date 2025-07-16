package com.yesul.community.model.entity;

import com.yesul.common.BaseTimeEntity;
import com.yesul.community.model.dto.PostRequestDto;
import com.yesul.like.model.entity.PostLike;
import com.yesul.user.model.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
@Entity
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

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0;

    @Column(name = "thumbnail", columnDefinition = "TEXT")
    private String thumbnail;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<PostLike> likes = new ArrayList<>();

    public void update(PostRequestDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.thumbnail = dto.getThumbnail();
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    // 이미지 삭제후 재생성
    public void clearImages() {
        if (images != null) {
            new ArrayList<>(images).forEach(image -> {
                image.setPost(null);  // 연관관계 끊기
            });
            images.clear();  // 한 번에 비우기
        }
    }

    public void addImage(PostImage image) {
        if (images == null) {
            images = new ArrayList<>();
        }
        images.add(image);
        image.setPost(this);
    }
}