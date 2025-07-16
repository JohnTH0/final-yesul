package com.yesul.like.controller;

import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.yesul.user.service.PrincipalDetails;
import com.yesul.like.service.AlcoholLikeService;

@Tag(name = "술 좋아요 API", description = "사용자가 술에 좋아요를 토글합니다.")
@Controller
@RequiredArgsConstructor
@RequestMapping("/alcohol-likes")
public class AlcoholLikeController {

    private final AlcoholLikeService alcoholLikeService;

    @Operation(summary = "술 좋아요 토글",
            description = "해당 ID의 술에 대해 좋아요를 누르거나 취소합니다.",
            parameters = { @Parameter(name="alcoholId", description="대상 술의 ID", required=true)},
            responses = { @ApiResponse(responseCode="200", description="토글 결과",
                    content = @Content(mediaType="application/json",
                    schema = @Schema( example = "{\"liked\":true,\"likeCount\":42}"))),
                    @ApiResponse(responseCode="401", description="로그인 필요"),
                    @ApiResponse(responseCode="404", description="술을 찾을 수 없음")
            }
    )
    @PostMapping("/{alcoholId}")
    @ResponseBody
    public Map<String,Object> toggleLike(@PathVariable Long alcoholId,
                                         @AuthenticationPrincipal PrincipalDetails principal) {
        Long userId = principal.getUser().getId();
        boolean liked = alcoholLikeService.toggleLike(alcoholId, userId);
        int count   = alcoholLikeService.getLikeCount(alcoholId);
        Map<String,Object> result = new HashMap<>();
        result.put("liked", liked);
        result.put("likeCount", count);
        return result;
    }
}
