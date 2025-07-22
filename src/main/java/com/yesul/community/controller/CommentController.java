package com.yesul.community.controller;

import com.yesul.community.model.dto.request.CommentRequestDto;
import com.yesul.community.service.ActivityDuplicateCheckService;
import com.yesul.community.service.CommentService;
import com.yesul.user.service.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.yesul.community.service.PointService;
import com.yesul.community.model.entity.enums.PointType;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Tag(name = "댓글", description = "댓글 관련 API")
@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final PointService pointService;
    private final ActivityDuplicateCheckService activityDuplicateCheckService;


    @PostMapping
    @Operation(summary = "댓글 저장", description = "댓글을 저장합니다.")
    public String save(@ModelAttribute CommentRequestDto dto,
                       @AuthenticationPrincipal PrincipalDetails principalDetails,
                       RedirectAttributes redirectAttributes) {

        Long userId = principalDetails.getUser().getId();

        // 1. Redis 중복 체크 (내용 검사 X, 단순 시간 제한)
        if (activityDuplicateCheckService.isDuplicate(userId, PointType.COMMENT_CREATE)) {
            redirectAttributes.addFlashAttribute("error", "20초 이내에는 중복 댓글 작성이 불가합니다.");
            return "redirect:/community/" + dto.getBoardName() + "/" + dto.getPostId();
        }

        // 2. 댓글 저장
        commentService.save(dto, userId);

        // 3. 포인트 적립
        pointService.earnPoint(userId, PointType.COMMENT_CREATE);

        // 4. Redis 저장 (TTL: 20초)
        activityDuplicateCheckService.saveActivity(userId, PointType.COMMENT_CREATE, 20);

        return "redirect:/community/" + dto.getBoardName() + "/" + dto.getPostId();
    }

    // 댓글 조회 (미사용 중)
    @GetMapping("/{postId}")
    public String getComments(@PathVariable Long postId) {
        return "댓글 리스트 예정";
    }


    @PostMapping("/delete")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
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