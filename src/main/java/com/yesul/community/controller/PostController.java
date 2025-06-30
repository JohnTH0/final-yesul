package com.yesul.community.controller;

import com.yesul.community.model.dto.PostRequestDto;
import com.yesul.community.model.dto.PostResponseDto;
import com.yesul.community.service.PostImageService;
import com.yesul.community.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/community")
public class PostController {

    private final PostService postService;
    private final PostImageService postImageService;

    @GetMapping("/")
    public String redirectToRecipe() {
        return "redirect:/community/recipe";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("postRequestDto", new PostRequestDto());
        return "community/postCreate";
    }
    @PostMapping("/create")
    public String createPost(@ModelAttribute PostRequestDto postRequestDto) {
        Long userId = 1L; // 임시: 로그인 기능 붙으면 수정 예정

        // 썸네일 자동 추출 (비어 있을 경우)
        if (postRequestDto.getThumbnail() == null || postRequestDto.getThumbnail().isBlank()) {
            String extractedThumbnail = postImageService.extractFirstImageUrl(postRequestDto.getContent());
            postRequestDto.setThumbnail(extractedThumbnail);
        }

        // 게시글 저장
        PostResponseDto createdPost = postService.createPost(postRequestDto, userId);

        // 리다이렉트
        String board = createdPost.getBoardName(); // 예: recipe, info, free
        Long postId = createdPost.getId();
        return "redirect:/community/" + board + "/" + postId;
    }
    @GetMapping({"/recipe", "/recipe/"})
    public String recipeList(Model model) {
        List<PostResponseDto> postList = postService.findAllByBoardName("recipe");
        model.addAttribute("postList", postList);
        System.out.println("레시피 글 수: " + postList.size());
        return "community/postRecipe";
    }

    @GetMapping("/recipe/{id}")
    public String recipeDetail(@PathVariable Long id, Model model) {
        try {
            PostResponseDto post = postService.findById(id);
            model.addAttribute("post", post);
            return "community/postRecipeDetail";
        } catch (Exception e) {
            System.err.println("게시글 상세 조회 에러: " + e.getMessage());
            return "redirect:/community/recipe"; // 또는 에러 페이지
        }
    }

    @GetMapping("/info")
    public String infoList(Model model) {
        return "community/postInfo";
    }

    @GetMapping("/info/{id}")
    public String infoDetail(@PathVariable Long id, Model model) {
        return "community/postInfoDetail";
    }

    @GetMapping("/free")
    public String freeList(Model model) {
        return "community/postFree";
    }

    @GetMapping("/free/{id}")
    public String freeDetail(@PathVariable Long id, Model model) {
        return "community/postFreeDetail";
    }
}