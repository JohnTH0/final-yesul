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
        return postRepository.searchByBoardNameAndKeyword(boardName, keyword, pageable)
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

                // 현재 사용자가 작성자인지 확인
                boolean isAuthor = post.getUser().getId().equals(userId);
                dto.setIsAuthor(isAuthor);
            } catch (Exception e) {
                dto.setLikedByMe(false);
                dto.setIsAuthor(false);
            }
        } else {
            dto.setLikedByMe(false);
            dto.setIsAuthor(false);
        }

        // 댓글 변환
        List<CommentResponseDto> commentDtos = post.getComments() != null ?
                post.getComments().stream()
                        .map(comment -> CommentResponseDto.from(comment, userId))
                        .toList() :
                new ArrayList<>();
        dto.setComments(commentDtos);

        return dto;
    }

    @Override
    public PostRequestDto findPostForEdit(Long postId, Long userId) {
        Post post = postRepository.findByIdWithImages(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }
        return PostRequestDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .thumbnail(post.getThumbnail())
                .boardName(post.getBoardName())
                .imageUrls(postImageService.getImageUrlsByPost(post))
                .build();
    }

    // 게시글 수정
    @Transactional
    @Override
    public void updatePost(Long postId, PostRequestDto postRequestDto, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        // 필수 필드 검증
        if (postRequestDto.getContent() == null || postRequestDto.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("내용을 입력해주세요.");
        }

        if (postRequestDto.getTitle() == null || postRequestDto.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("제목을 입력해주세요.");
        }

        // 게시글 정보 수정
        post.update(postRequestDto);

        // 기존 이미지 삭제
        if (post.getImages() != null) {
            List<PostImage> existingImages = new ArrayList<>(post.getImages());
            for (PostImage image : existingImages) {
                postImageRepository.delete(image);
            }
            post.getImages().clear();
        }

        // 새 이미지 추가
        if (postRequestDto.getImageUrls() != null && !postRequestDto.getImageUrls().isEmpty()) {
            for (String imageUrl : postRequestDto.getImageUrls()) {
                if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                    PostImage postImage = PostImage.builder()
                            .imageUrl(imageUrl.trim())
                            .build();
                    post.addImage(postImage);
                    postImageRepository.save(postImage);
                }
            }
        }

        // 썸네일이 비어있으면 첫 번째 이미지로 설정
        if ((postRequestDto.getThumbnail() == null || postRequestDto.getThumbnail().trim().isEmpty())
                && postRequestDto.getContent() != null) {
            String extractedThumbnail = postImageService.extractFirstImageUrl(postRequestDto.getContent());
            if (extractedThumbnail != null && !extractedThumbnail.trim().isEmpty()) {
                post.setThumbnail(extractedThumbnail);
            }
        }
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long postId, Long userId){
        // 1. 게시글 가져오기
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        // 2. 작성자 확인
        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        // 3. 이미지 삭제
        for (PostImage image : post.getImages()) {
            postImageService.deleteImage(image.getImageUrl());
        }

        // 4. 게시글 삭제 (PostImage, Like 등도 같이 삭제)
        postRepository.delete(post);
    }


    // 좋아요
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
                                .map(comment ->  CommentResponseDto.from(comment, null))
                                .toList() :
                        new ArrayList<>()
        );

        return dto;
    }
}