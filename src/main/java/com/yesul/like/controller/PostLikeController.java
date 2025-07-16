package com.yesul.like.controller;

import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;

import com.yesul.user.service.PrincipalDetails;
import com.yesul.like.service.PostLikeService;

@Tag(name = "게시글 좋아요 API", description = "사용자가 게시글에 좋아요를 토글합니다.")
@Controller
@RequiredArgsConstructor
@RequestMapping("/likes")
public class PostLikeController {

    private final PostLikeService likeService;

    @Operation(summary = "게시글 좋아요 토글", description = "해당 ID의 게시글에 대해 좋아요를 누르거나 취소합니다.",
            parameters = { @Parameter(name="postId", description="대상 게시글의 ID", required=true)},
            responses = { @ApiResponse(responseCode="200", description="토글 결과",
                    content = @Content(mediaType="application/json",
                    schema = @Schema( example = "{\"liked\":false,\"likeCount\":10}" ))),
                    @ApiResponse(responseCode="401", description="로그인 필요"),
                    @ApiResponse(responseCode="404", description="게시글을 찾을 수 없음")
            }
    )
    @PostMapping("/{postId}")
    @ResponseBody
    public Map<String, Object> toggleLike(@PathVariable Long postId,
                                          @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long userId = principalDetails.getUser().getId();
        boolean liked = likeService.toggleLike(postId, userId);
        int likeCount = likeService.getLikeCount(postId);

        Map<String, Object> result = new HashMap<>();
        result.put("liked", liked);
        result.put("likeCount", likeCount);
        return result;
    }
}