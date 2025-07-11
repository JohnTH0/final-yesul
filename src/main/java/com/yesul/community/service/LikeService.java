package com.yesul.community.service;

import java.util.List;

import com.yesul.community.model.dto.LikePostDto;

public interface LikeService {
    boolean toggleLike(Long postId, Long userId);
    int getLikeCount(Long postId);

    List<LikePostDto> getLikedPosts(Long userId);
}
