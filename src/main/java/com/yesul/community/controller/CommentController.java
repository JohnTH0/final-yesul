package com.yesul.community.controller;

import com.yesul.community.model.dto.CommentRequestDto;
import com.yesul.community.service.CommentService;
import com.yesul.user.service.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    // 댓글 저장
    @PostMapping
    public String saveComment(@ModelAttribute CommentRequestDto dto,
                              @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Long userId = principalDetails.getUser().getId(); // 로그인 유저 ID
        commentService.save(dto, userId); // userId 따로 넘김
        return "redirect:/community/" + dto.getBoardName() + "/" + dto.getPostId();
    }

    // 댓글 조회 (미사용 중)
    @GetMapping("/{postId}")
    public String getComments(@PathVariable Long postId) {
        return "댓글 리스트 예정";
    }
}