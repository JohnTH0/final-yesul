package com.yesul.community.service;

public interface LikeService {
    boolean toggleLike(Long postId, Long userId); // 좋아요 토글
    int getLikeCount(Long postId);                // 좋아요 수 토글
}
