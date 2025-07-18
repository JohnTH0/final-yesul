package com.yesul.main.controller;

import com.yesul.community.model.dto.response.PostResponseDto;
import com.yesul.community.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping
@Controller
public class MainController {

    private final PostService postService;

    public MainController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<PostResponseDto> randomPopularPosts = postService.getRandomPopularPosts(5);
        model.addAttribute("randomPopularPosts", randomPopularPosts);
        return "index";
    }


}