package com.yesul.community.repository;

import com.yesul.community.model.dto.LikePostDto;
import com.yesul.community.model.entity.Like;
import com.yesul.community.model.entity.Post;
import com.yesul.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostAndUser(Post post, User user);
    // 좋아요 여부 확인
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    // 게시글 ID 기준 좋아요 수
    int countByPostId(Long postId);

    @Query("""
        SELECT new com.yesul.community.model.dto.LikePostDto(
          l.post.id,
          l.post.title,
          l.post.thumbnail,
          l.createdAt
        )
        FROM Like l
        WHERE l.user.id = :userId
        ORDER BY l.createdAt DESC
    """)
    List<LikePostDto> findLikedPostsByUserId(Long userId);
}