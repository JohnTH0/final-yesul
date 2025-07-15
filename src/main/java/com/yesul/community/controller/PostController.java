package com.yesul.community.controller;

import com.yesul.community.model.dto.PostRequestDto;
import com.yesul.community.model.dto.PostResponseDto;
import com.yesul.community.model.entity.enums.PointType;
import com.yesul.community.service.PointService;
import com.yesul.community.service.PostImageService;
import com.yesul.community.service.PostService;
import com.yesul.user.service.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/community")
public class PostController {

    private final PostService postService;
    private final PostImageService postImageService;
    private final PointService pointService;

    @GetMapping
    public String redirectToRecipe() {
        return "redirect:/community/recipe";
    }

    /**
     * 게시판별 게시글 목록 조회 및 검색
     */
    @GetMapping("/{boardName}")
    public String boardList(@PathVariable String boardName,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(required = false) String keyword,
                            Model model) {

        Pageable pageable = PageRequest.of(page, 6, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<PostResponseDto> postPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            postPage = postService.searchByBoardNameAndKeyword(boardName, keyword, pageable);
        } else {
            postPage = postService.findByBoardNamePaged(boardName, pageable);
        }

        model.addAttribute("postPage", postPage);
        model.addAttribute("postList", postPage.getContent());
        model.addAttribute("boardName", boardName);
        model.addAttribute("paramKeyword", keyword);

        String viewName = switch (boardName) {
            case "recipe" -> "community/postRecipe";
            case "info" -> "community/postInfo";
            case "free" -> "community/postFree";
            default -> throw new IllegalArgumentException("존재하지 않는 게시판입니다: " + boardName);
        };

        return viewName;
    }

    /**
     * 게시글 상세 조회
     */
    @GetMapping("/{boardName}/{id}")
    public String postDetail(@PathVariable String boardName,
                             @PathVariable Long id,
                             @AuthenticationPrincipal PrincipalDetails principalDetails,
                             Model model) {
        Long userId = (principalDetails != null) ? principalDetails.getUser().getId() : null;
        PostResponseDto post = postService.findById(id, userId);

        model.addAttribute("post", post);
        model.addAttribute("board_name", boardName);
        model.addAttribute("isLoggedIn", principalDetails != null);
        return "community/postDetail";
    }

    /**
     * 게시글 작성 폼으로 이동
     */
    @GetMapping("/create")
    public String createForm(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails == null) {
            return "redirect:/login";
        }
        model.addAttribute("postRequestDto", new PostRequestDto());
        return "community/postCreate";
    }

    /**
     * 게시글 등록 처리
     */
    @PostMapping("/create")
    public String createPost(@ModelAttribute PostRequestDto postRequestDto,
                             @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails == null) {
            return "redirect:/login";
        }

        Long userId = principalDetails.getUser().getId();

        if (postRequestDto.getThumbnail() == null || postRequestDto.getThumbnail().isBlank()) {
            String extractedThumbnail = postImageService.extractFirstImageUrl(postRequestDto.getContent());
            if (extractedThumbnail != null && !extractedThumbnail.trim().isEmpty()) {
                postRequestDto.setThumbnail(extractedThumbnail);
            }
        }

        PostResponseDto createdPost = postService.createPost(postRequestDto, userId);

        pointService.earnPoint(userId, PointType.POST_CREATE, String.valueOf(createdPost.getId()));

        return "redirect:/community/" + createdPost.getBoardName() + "/" + createdPost.getId();
    }

    /**
     * 게시글 수정 폼으로 이동
     */
    @GetMapping("/{boardName}/{id}/edit")
    public String editForm(@PathVariable String boardName,
                           @PathVariable Long id,
                           Model model,
                           @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails == null) {
            return "redirect:/login";
        }

        Long userId = principalDetails.getUser().getId();

        try {
            PostRequestDto postRequestDto = postService.findPostForEdit(id, userId);
            model.addAttribute("postRequestDto", postRequestDto);
            model.addAttribute("boardName", boardName);
            model.addAttribute("isLoggedIn", true);
            return "community/postEdit";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/community/" + boardName + "/" + id;
        }
    }

    /**
     * 게시글 수정 처리
     */
    @PostMapping("/{boardName}/{id}/edit")
    public String updatePost(@ModelAttribute PostRequestDto postRequestDto,
                             @PathVariable String boardName,
                             @PathVariable Long id,
                             Model model,
                             @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails == null) {
            return "redirect:/login";
        }

        if (postRequestDto.getContent() == null || postRequestDto.getContent().trim().isEmpty()) {
            model.addAttribute("error", "내용을 입력해주세요.");
            return "redirect:/community/" + boardName + "/" + id + "/edit";
        }

        if (postRequestDto.getTitle() == null || postRequestDto.getTitle().trim().isEmpty()) {
            model.addAttribute("error", "제목을 입력해주세요.");
            return "redirect:/community/" + boardName + "/" + id + "/edit";
        }

        Long userId = principalDetails.getUser().getId();

        try {
            postService.updatePost(id, postRequestDto, userId);
            return "redirect:/community/" + boardName + "/" + id;
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/community/" + boardName + "/" + id + "/edit";
        }
    }

    /**
     * 게시글 삭제 처리
     */
    @PostMapping("/{boardName}/{id}/delete")
    public String deletePost(@PathVariable String boardName,
                             @PathVariable Long id,
                             @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails == null) {
            return "redirect:/login";
        }

        Long userId = principalDetails.getUser().getId();
        postService.deletePost(id, userId);
        pointService.usePoint(userId, PointType.POST_CREATE);
        return "redirect:/community/" + boardName;
    }
}