package com.yesul.community.service;

import com.yesul.community.model.dto.request.PostRequestDto;
import com.yesul.community.model.dto.response.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    PostResponseDto createPost(PostRequestDto requestDto, Long userId);
    Page<PostResponseDto> findByBoardNamePaged(String boardName, Pageable pageable,Long userId);
    List<PostResponseDto> findAllByBoardName(String boardName);
    Page<PostResponseDto> searchByBoardNameAndKeyword(String boardName, String keyword, Pageable pageable);
    PostResponseDto findById(Long id, Long userId);
    PostRequestDto findPostForEdit(Long postId, Long userId);
    void updatePost(Long postId, PostRequestDto postRequestDto, Long userId);
    void deletePost(Long postId, Long userId);
    boolean isLikedByUser(Long postId, Long userId);
    List<PostResponseDto> getRandomPopularPosts(int count);
}