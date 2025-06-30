package com.yesul.community.service;

import com.yesul.community.model.dto.PostRequestDto;
import com.yesul.community.model.dto.PostResponseDto;

import java.util.List;

public interface PostService {
    PostResponseDto createPost(PostRequestDto requestDto, Long userId);
    List<PostResponseDto> findAllByBoardName(String boardName);
    PostResponseDto findById(Long id);
}