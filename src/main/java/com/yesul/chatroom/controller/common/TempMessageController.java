package com.yesul.chatroom.controller.common;

import static com.yesul.common.constants.RedisConstants.TEMP_MESSAGE_DB_INDEX;

import com.yesul.chatroom.service.TempMessageService;
import com.yesul.exception.handler.AuthenticationNotFoundException;
import com.yesul.user.service.PrincipalDetails;
import com.yesul.admin.model.dto.auth.LoginAdmin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "TempMessage", description = "임시 메시지 관련 API")
@RestController
@RequestMapping("/chatroom/temp-message")
@RequiredArgsConstructor
public class TempMessageController {

    private final TempMessageService tempMessageService;

    @PostMapping
    @Operation(summary = "임시 메시지 저장", description = "채팅방에 작성한 임시 메시지를 1시간동안 Redis에 저장합니다.")
    public void saveTempMessage(
            @AuthenticationPrincipal Object principal,
            @RequestParam Long chatRoomId,
            @RequestParam String message
    ) {
        Long userId = extractUserId(principal);
        tempMessageService.saveTempMessage(chatRoomId, userId, message,  TEMP_MESSAGE_DB_INDEX);
    }

    @GetMapping
    @Operation(summary = "임시 메시지 조회", description = "채팅방에 작성했던 임시 메시지를 Redis에서 조회해옵니다.")
    public String getTempMessage(
            @AuthenticationPrincipal Object principal,
            @RequestParam Long chatRoomId
    ) {
        Long userId = extractUserId(principal);
        String tempMessage = tempMessageService.getTempMessage(chatRoomId, userId,TEMP_MESSAGE_DB_INDEX);
        return tempMessage;
    }

    @DeleteMapping
    @Operation(summary = "임시 메시지 삭제", description = "채팅방에 작성한 임시 메시지를 Redis에서 삭제합니다.")
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
            throw new AuthenticationNotFoundException("인증 정보가 존재하지 않습니다.");
        }
    }
}
