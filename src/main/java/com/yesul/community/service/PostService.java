package com.yesul.community.service;

import com.yesul.community.model.dto.PostRequestDto;
import com.yesul.community.model.dto.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    PostResponseDto createPost(PostRequestDto requestDto, Long userId);
    Page<PostResponseDto> findByBoardNamePaged(String boardName, Pageable pageable);
    List<PostResponseDto> findAllByBoardName(String boardName);
    Page<PostResponseDto> searchByBoardNameAndKeyword(String boardName, String keyword, Pageable pageable);
    PostResponseDto findById(Long id, Long userId);
    void updatePost(Long postId, PostRequestDto postRequestDto, Long userId);
    boolean isLikedByUser(Long postId, Long userId);
}