package com.yesul.chatroom.controller.common;

import static com.yesul.common.constants.RedisConstants.TEMP_MESSAGE_DB_INDEX;

import com.yesul.chatroom.service.TempMessageService;
import com.yesul.user.service.PrincipalDetails;
import com.yesul.admin.model.dto.auth.LoginAdmin;
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
            @AuthenticationPrincipal Object principal,
            @RequestParam Long chatRoomId,
            @RequestParam String message
    ) {
        Long userId = extractUserId(principal);
        tempMessageService.saveTempMessage(chatRoomId, userId, message,  TEMP_MESSAGE_DB_INDEX);
    }

    @GetMapping
    public String getTempMessage(
            @AuthenticationPrincipal Object principal,
            @RequestParam Long chatRoomId
    ) {
        Long userId = extractUserId(principal);
        String tempMessage = tempMessageService.getTempMessage(chatRoomId, userId,TEMP_MESSAGE_DB_INDEX);
        return tempMessage;
    }

    @DeleteMapping
    public void deleteTempMessage(
            @AuthenticationPrincipal Object principal,
            @RequestParam Long chatRoomId
    ) {
        Long userId = extractUserId(principal);
        tempMessageService.deleteTempMessage(chatRoomId, userId,TEMP_MESSAGE_DB_INDEX);
    }

    /**
     *  공통 유저 식별자 추출 (PrincipalDetails or LoginAdmin)
     */
    private Long extractUserId(Object principal) {
        if (principal instanceof PrincipalDetails details) {
            return details.getUser().getId();
        } else if (principal instanceof LoginAdmin admin) {
            return admin.getId();
        } else {
            throw new RuntimeException("인증 정보가 존재하지 않습니다.");
        }
    }
}
