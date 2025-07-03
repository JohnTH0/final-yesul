package com.yesul.community.repository;

import com.yesul.community.model.entity.Like;
import com.yesul.community.model.entity.Post;
import com.yesul.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostAndUser(Post post, User user);
    // 좋아요 여부 확인
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    // 게시글 ID 기준 좋아요 수
    int countByPostId(Long postId);
}