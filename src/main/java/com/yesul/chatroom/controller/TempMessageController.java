package com.yesul.chatroom.controller;

import com.yesul.chatroom.service.TempMessageService;
import com.yesul.user.service.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/chatroom/temp-message")
@RequiredArgsConstructor
public class TempMessageController {

    private final TempMessageService tempMessageService;

    @PostMapping
    public void saveTempMessage(
            @AuthenticationPrincipal PrincipalDetails principal,
            @RequestParam Long chatRoomId,
            @RequestParam String message
    ) {
        Long userId = principal.getUser().getId();
        tempMessageService.saveTempMessage(chatRoomId, userId, message);
    }

    @GetMapping
    public String getTempMessage(
            @AuthenticationPrincipal PrincipalDetails principal,
            @RequestParam Long chatRoomId
    ) {
        Long userId = principal.getUser().getId();
        return tempMessageService.getTempMessage(chatRoomId, userId);
    }

    @DeleteMapping
    public void deleteTempMessage(
            @AuthenticationPrincipal PrincipalDetails principal,
            @RequestParam Long chatRoomId
    ) {
        Long userId = principal.getUser().getId();
        tempMessageService.deleteTempMessage(chatRoomId, userId);
    }
}
