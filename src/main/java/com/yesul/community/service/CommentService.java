package com.yesul.community.service;

import com.yesul.community.model.dto.CommentRequestDto;

public interface CommentService {
    Long save(CommentRequestDto dto, Long userId);
    void delete(Long commentId, Long userId);
}
