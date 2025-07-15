package com.yesul.community.controller;

import com.yesul.community.model.dto.CommentRequestDto;
import com.yesul.community.service.CommentService;
import com.yesul.user.service.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.yesul.community.service.PointService;
import com.yesul.community.model.entity.enums.PointType;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final PointService pointService;

    // 댓글 저장
    @PostMapping
    public String save(@ModelAttribute CommentRequestDto dto,
                       @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Long userId = principalDetails.getUser().getId(); // 로그인 유저 ID
        Long commentId = commentService.save(dto, userId);
        pointService.earnPoint(userId, PointType.COMMENT_CREATE, String.valueOf(commentId));
        return "redirect:/community/" + dto.getBoardName() + "/" + dto.getPostId();
    }

    // 댓글 조회 (미사용 중)
    @GetMapping("/{postId}")
    public String getComments(@PathVariable Long postId) {
        return "댓글 리스트 예정";
    }

    // 댓글 삭제
    @PostMapping("/delete")
    public String delete(@RequestParam Long commentId,
                         @RequestParam String boardName,
                         @RequestParam Long postId,
                         @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long userId = principalDetails.getUser().getId();
        commentService.delete(commentId, userId);
        pointService.usePoint(userId, PointType.COMMENT_CREATE);
        return "redirect:/community/" + boardName + "/" + postId;
    }
}