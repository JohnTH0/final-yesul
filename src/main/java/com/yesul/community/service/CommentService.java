package com.yesul.community.service;

import com.yesul.community.model.dto.CommentRequestDto;

public interface CommentService {
    void save(CommentRequestDto dto, long userId);
    void delete(Long commentId, Long userId);
}
