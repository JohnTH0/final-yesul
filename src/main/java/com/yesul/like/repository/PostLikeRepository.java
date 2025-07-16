package com.yesul.like.repository;

import com.yesul.like.model.dto.PostLikeDto;
import com.yesul.like.model.entity.PostLike;
import com.yesul.community.model.entity.Post;
import com.yesul.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostAndUser(Post post, User user);
    // 좋아요 여부 확인
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    // 게시글 ID 기준 좋아요 수
    int countByPostId(Long postId);

    @Query("""
        SELECT new com.yesul.like.model.dto.PostLikeDto(
          l.post.id,
          l.post.title,
          l.post.thumbnail,
          l.createdAt
        )
        FROM PostLike l
        WHERE l.user.id = :userId
        ORDER BY l.createdAt DESC
    """)
    List<PostLikeDto> findLikedPostsByUserId(Long userId);
}