package com.yesul.community.repository;

import com.yesul.community.model.dto.response.PostResponseDto;

import java.util.List;

public interface PostRepositoryCustom {
    List<PostResponseDto> findPopularPostsByLikes();
}
