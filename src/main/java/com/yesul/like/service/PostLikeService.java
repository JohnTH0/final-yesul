package com.yesul.like.service;

import java.util.List;

import com.yesul.like.model.dto.PostLikeDto;

public interface PostLikeService {
    boolean toggleLike(Long postId, Long userId);
    int getLikeCount(Long postId);

    List<PostLikeDto> getLikedPosts(Long userId);
}
