package com.yesul.community.service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.yesul.community.model.dto.LikePostDto;
import com.yesul.community.model.entity.Like;
import com.yesul.community.model.entity.Post;
import com.yesul.user.model.entity.User;
import com.yesul.community.repository.LikeRepository;
import com.yesul.community.repository.PostRepository;
import com.yesul.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public boolean toggleLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        // 이미 좋아요가 있는지 확인
        Optional<Like> existing = likeRepository.findByPostAndUser(post, user);

        if (existing.isPresent()) {
            // 좋아요 이미 있음 → 삭제
            likeRepository.delete(existing.get());
            return false;
        } else {
            // 좋아요 없음 → 생성
            Like like = Like.builder()
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
    public List<LikePostDto> getLikedPosts(Long userId) {
        return likeRepository.findLikedPostsByUserId(userId);
    }
}
