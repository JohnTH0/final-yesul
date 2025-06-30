package com.yesul.community.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    // 추후 댓글 등록/조회/삭제 기능 추가 예정

    @PostMapping
    public String createComment() {
        return "댓글 등록 예정";
    }

    @GetMapping("/{postId}")
    public String getComments(@PathVariable Long postId) {
        return "댓글 리스트 예정";
    }
}