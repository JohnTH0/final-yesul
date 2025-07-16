package com.yesul.like.service;

import java.util.List;
import java.util.Optional;

import com.yesul.like.model.entity.PostLike;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.yesul.like.model.dto.PostLikeDto;
import com.yesul.community.model.entity.Post;
import com.yesul.user.model.entity.User;
import com.yesul.like.repository.PostLikeRepository;
import com.yesul.community.repository.PostRepository;
import com.yesul.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public boolean toggleLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        Optional<PostLike> existing = likeRepository.findByPostAndUser(post, user);

        if (existing.isPresent()) {
            likeRepository.delete(existing.get());
            return false;
        } else {
            PostLike like = PostLike.builder()
                    .post(post)
                    .user(user)
                    .build();
            likeRepository.save(like);
            return true;
        }
    }
    @Override
    public int getLikeCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }

    @Override
    public List<PostLikeDto> getLikedPosts(Long userId) {
        return likeRepository.findLikedPostsByUserId(userId);
    }
}
