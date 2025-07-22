package com.yesul.community.service;

import com.yesul.community.model.dto.request.CommentRequestDto;
import com.yesul.community.model.entity.Comment;
import com.yesul.community.model.entity.Post;
import com.yesul.community.repository.CommentRepository;
import com.yesul.community.repository.PostRepository;
import com.yesul.exception.handler.UserNotFoundException;
import com.yesul.notification.service.NotificationService;
import com.yesul.user.model.entity.User;
import com.yesul.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final NotificationService notificationService;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public Long save(CommentRequestDto dto, Long userId) {
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new RuntimeException("해당 게시글을 찾을 수 없습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(dto.getContent())
                .build();

        commentRepository.save(comment);

        notificationService.sendPostOwnerCommentNotification(dto.getPostId(), userId, post.getUser().getId(), post.getBoardName());

        return comment.getId();
    }

    @Override
    public void delete(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));
        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("본인 댓글만 삭제 가능합니다");
        }
        commentRepository.delete(comment);
    }

}