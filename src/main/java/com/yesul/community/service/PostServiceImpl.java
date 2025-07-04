package com.yesul.community.service;

import com.yesul.community.model.dto.CommentResponseDto;
import com.yesul.community.model.dto.PostRequestDto;
import com.yesul.community.model.dto.PostResponseDto;
import com.yesul.community.model.entity.Post;
import com.yesul.community.model.entity.PostImage;
import com.yesul.community.repository.LikeRepository;
import com.yesul.community.repository.PostImageRepository;
import com.yesul.community.repository.PostRepository;
import com.yesul.user.model.entity.User;
import com.yesul.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostImageRepository postImageRepository;
    private final LikeRepository likeRepository;
    private final PostImageService postImageService;

    // 게시글 등록 처리
    @Transactional
    @Override
    public PostResponseDto createPost(PostRequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 유저가 없습니다: " + userId));

        Post post = Post.builder()
                .user(user)
                .boardName(requestDto.getBoardName())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .thumbnail(requestDto.getThumbnail())
                .viewCount(0)
                .build();

        postRepository.save(post);

        if (requestDto.getImageUrls() != null) {
            for (String imageUrl : requestDto.getImageUrls()) {
                PostImage postImage = PostImage.builder()
                        .imageUrl(imageUrl)
                        .build();

                post.addImage(postImage);
                postImageRepository.save(postImage);
            }
        }

        return convertToDto(postRepository.findByIdWithImages(post.getId())
                .orElseThrow(() -> new IllegalArgumentException("조회 실패")));
    }

    // 게시글 리스트 조회 (페이징)
    @Override
    public Page<PostResponseDto> findByBoardNamePaged(String boardName, Pageable pageable) {
        return postRepository.findByBoardName(boardName, pageable)
                .map(this::convertToDto); // Post → DTO로 변환
    }

    // 키워드 검색 (게시판 + 제목에 포함되는 키워드)
    @Override
    public Page<PostResponseDto> searchByBoardNameAndKeyword(String boardName, String keyword, Pageable pageable) {
        return postRepository.findByBoardNameAndTitleContainingIgnoreCase(boardName, keyword, pageable)
                .map(this::convertToDto);
    }

    @Override
    public PostResponseDto findById(Long id, Long userId) {
        // images까지 fetch join으로 가져옴
        Post post = postRepository.findByIdWithImages(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        PostResponseDto dto = PostResponseDto.from(post);
        dto.setLikeCount(post.getLikes() != null ? post.getLikes().size() : 0);

        // 이미지 URL 리스트
        dto.setImageUrls(postImageService.getImageUrlsByPost(post));

        // 로그인한 사용자인 경우에만 좋아요 여부 확인
        if (userId != null) {
            try {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 ID의 유저가 없습니다."));
                boolean likedByMe = likeRepository.findByPostAndUser(post, user).isPresent();
                dto.setLikedByMe(likedByMe);
            } catch (Exception e) {
                dto.setLikedByMe(false);
            }
        } else {
            dto.setLikedByMe(false);
        }

        // 댓글 변환
        List<CommentResponseDto> commentDtos = post.getComments() != null ?
                post.getComments().stream()
                        .map(CommentResponseDto::from)
                        .toList() :
                new ArrayList<>();
        dto.setComments(commentDtos);

        return dto;
    }

    // 게시글 수정
    @Transactional
    public void updatePost(Long postId, PostRequestDto postRequestDto, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        post.update(postRequestDto);
    }

    @Override
    public boolean isLikedByUser(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        return post.getLikes().stream()
                .anyMatch(like -> like.getUser().getId().equals(userId));
    }

    // 전체 리스트 조회 (페이징 없이)
    @Override
    public List<PostResponseDto> findAllByBoardName(String boardName) {
        return postRepository.findByBoardName(boardName).stream()
                .map(this::convertToDto)
                .toList();
    }

    // 공통 DTO 변환 메서드 (좋아요 수, 댓글 포함)
    private PostResponseDto convertToDto(Post post) {
        PostResponseDto dto = PostResponseDto.from(post);

        // 좋아요 수 (null 체크)
        dto.setLikeCount(post.getLikes() != null ? post.getLikes().size() : 0);

        // 이미지 URL 리스트 (이미지 서비스 사용)
        dto.setImageUrls(postImageService.getImageUrlsByPost(post));

        // 댓글 리스트 (간단 변환, null 체크)
        dto.setComments(
                post.getComments() != null ?
                        post.getComments().stream()
                                .map(CommentResponseDto::from)
                                .toList() :
                        new ArrayList<>()
        );

        return dto;
    }
}