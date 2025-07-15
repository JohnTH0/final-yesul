package com.yesul.community.repository;

import com.yesul.community.model.dto.PostResponseDto;

import java.util.List;

public interface PostRepositoryCustom {
    List<PostResponseDto> findPopularPostsByLikes();
}
