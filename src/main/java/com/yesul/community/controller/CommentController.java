package com.yesul.community.controller;

import com.yesul.community.model.dto.CommentRequestDto;
import com.yesul.community.service.ActivityDuplicateCheckService;
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
    private final ActivityDuplicateCheckService activityDuplicateCheckService;

    // 댓글 저장
    @PostMapping
    public String save(@ModelAttribute CommentRequestDto dto,
                       @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Long userId = principalDetails.getUser().getId();

        // 1. Redis 중복 체크 (내용 검사 X, 단순 시간 제한)
        if (activityDuplicateCheckService.isDuplicate(userId, PointType.COMMENT_CREATE)) {
            throw new IllegalArgumentException("댓글은 잠시 후 다시 작성해주세요.");
        }

        // 2. 댓글 저장
        Long commentId = commentService.save(dto, userId);

        // 3. 포인트 적립
        pointService.earnPoint(userId, PointType.COMMENT_CREATE); // content 제거됨

        // 4. Redis 저장 (TTL: 20초)
        activityDuplicateCheckService.saveActivity(userId, PointType.COMMENT_CREATE, 20);

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