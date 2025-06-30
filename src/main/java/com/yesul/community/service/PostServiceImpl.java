package com.yesul.community.service;

import com.yesul.community.model.dto.PostRequestDto;
import com.yesul.community.model.dto.PostResponseDto;
import com.yesul.community.model.entity.Post;
import com.yesul.community.model.entity.PostImage;
import com.yesul.community.repository.PostImageRepository;
import com.yesul.user.model.entity.User;
import com.yesul.community.repository.PostRepository;
import com.yesul.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostImageRepository postImageRepository;

    @Transactional
    @Override
    public PostResponseDto createPost(PostRequestDto requestDto, Long userId) {
        // 유저 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 유저가 없습니다: " + userId));

        // Post 엔티티 생성
        Post post = Post.builder()
                .user(user)
                .boardName(requestDto.getBoardName())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .thumbnail(requestDto.getThumbnail())
                .viewCount(0) // 초기값
                .build();

        // 저장
        Post savedPost = postRepository.save(post);

        // Save PostImage entities if imageUrls are provided
        System.out.println("이미지 URL 목록: " + requestDto.getImageUrls());
        if (requestDto.getImageUrls() != null) {
            for (String imageUrl : requestDto.getImageUrls()) {
                PostImage postImage = PostImage.builder()
                        .post(savedPost)
                        .imageUrl(imageUrl)
                        .build();
                postImageRepository.save(postImage);
                System.out.println("저장된 이미지 URL: " + imageUrl);
            }
        }

        // 날짜 포맷팅
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedCreatedAt = savedPost.getCreatedAt().format(formatter);

        // 응답 DTO 생성
        return PostResponseDto.builder()
                .id(savedPost.getId())
                .title(savedPost.getTitle())
                .content(savedPost.getContent())
                .boardName(savedPost.getBoardName())
                .thumbnail(savedPost.getThumbnail())
                .nickname(user.getNickname())
                .createdAt(formattedCreatedAt)
                .viewCount(savedPost.getViewCount())
                .build();
    }

    @Override
    public List<PostResponseDto> findAllByBoardName(String boardName) {
        List<Post> posts = postRepository.findByBoardNameOrderByCreatedAtDesc(boardName);

        return posts.stream().map(post -> PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .boardName(post.getBoardName())
                .thumbnail(post.getThumbnail())
                .nickname(post.getUser().getNickname())
                .createdAt(post.getCreatedAt().toString())
                .viewCount(post.getViewCount())
                .build()
        ).toList();
    }

    @Override
    public PostResponseDto findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .boardName(post.getBoardName())
                .thumbnail(post.getThumbnail())
                .nickname(post.getUser().getNickname())
                .createdAt(post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .viewCount(post.getViewCount())
//                .likeCount(post.getLikes().size())       // 좋아요 수
//                .likedByMe(false)                        // 추후 로그인 유저 처리
//                .comments(List.of())                     // 댓글 기능
                .build();
    }

}