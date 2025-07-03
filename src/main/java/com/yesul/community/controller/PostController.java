package com.yesul.community.controller;

import com.yesul.community.model.dto.PostRequestDto;
import com.yesul.community.model.dto.PostResponseDto;
import com.yesul.community.service.PostImageService;
import com.yesul.community.service.PostService;
import com.yesul.user.service.PrincipalDetails;
import com.yesul.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/community")
public class PostController {

    private final PostService postService;
    private final PostImageService postImageService;

    // 커뮤니티 들어가면 레시피로 리다이렉트
    @GetMapping("/")
    public String redirectToRecipe() {
        return "redirect:/community/recipe";
    }

    @GetMapping("/create")
    public String createForm(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        // 로그인 체크
        if (principalDetails == null) {
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
        }

        model.addAttribute("postRequestDto", new PostRequestDto());
        return "community/postCreate";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute PostRequestDto postRequestDto,
                             @AuthenticationPrincipal PrincipalDetails principalDetails) {
        try {
            // 로그인 체크
            if (principalDetails == null) {
                return "redirect:/login"; // 로그인 페이지로 리다이렉트
            }

            Long userId = principalDetails.getUser().getId(); // 로그인한 유저 ID
            System.out.println("게시글 등록 시작 - userId: " + userId);
            System.out.println("게시판: " + postRequestDto.getBoardName());
            System.out.println("제목: " + postRequestDto.getTitle());

            // 썸네일 자동 추출 (비어 있을 경우)
            if (postRequestDto.getThumbnail() == null || postRequestDto.getThumbnail().isBlank()) {
                String extractedThumbnail = postImageService.extractFirstImageUrl(postRequestDto.getContent());
                if (extractedThumbnail != null && !extractedThumbnail.trim().isEmpty()) {
                    postRequestDto.setThumbnail(extractedThumbnail);
                }
            }

            // 게시글 저장
            PostResponseDto createdPost = postService.createPost(postRequestDto, userId);
            System.out.println("게시글 저장 완료 - postId: " + createdPost.getId());

            // 저장된 게시글로 리다이렉트
            String board = createdPost.getBoardName(); // 예: recipe, info, free
            Long postId = createdPost.getId();
            return "redirect:/community/" + board + "/" + postId;
        } catch (Exception e) {
            System.err.println("게시글 등록 중 에러 발생: " + e.getMessage());
            e.printStackTrace();
            throw e; // 에러를 다시 던져서 500 에러 페이지가 표시되도록 함
        }
    }

    @GetMapping("/{boardName}")
    public String boardList(@PathVariable String boardName,
                            @RequestParam(defaultValue = "0") int page,
                            Model model) {

        Pageable pageable = PageRequest.of(page, 6, Sort.by("createdAt").descending());
        Page<PostResponseDto> postPage = postService.findByBoardNamePaged(boardName, pageable);

        model.addAttribute("postPage", postPage);
        model.addAttribute("postList", postPage.getContent());

        String viewName = switch (boardName) {
            case "recipe" -> "community/postRecipe";
            case "info" -> "community/postInfo";
            case "free" -> "community/postFree";
            default -> throw new IllegalArgumentException("존재하지 않는 게시판입니다: " + boardName);
        };

        return viewName;
    }

    @GetMapping("/{boardName}/{id}")
    public String postDetail(@PathVariable String boardName,
                             @PathVariable Long id,
                             Model model,
                             @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Long userId = (principalDetails != null) ? principalDetails.getUser().getId() : null;

        // userId까지 포함해서 DTO 생성 (좋아요 여부, 댓글 포함)
        PostResponseDto post = postService.findById(id, userId);

        model.addAttribute("post", post);
        model.addAttribute("boardName", boardName);
        return "community/postDetail";
    }
}
