package com.yesul.community.controller;

import com.yesul.user.service.PrincipalDetails;
import com.yesul.community.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/community")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/likes/toggle/{postId}")
    @ResponseBody
    public Map<String, Object> toggleLike(@PathVariable Long postId,
                                          @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long userId = principalDetails.getUser().getId();  // 로그인 유저 ID
        boolean liked = likeService.toggleLike(postId, userId);
        int likeCount = likeService.getLikeCount(postId);

        Map<String, Object> result = new HashMap<>();
        result.put("liked", liked);
        result.put("likeCount", likeCount);
        return result;
    }
}