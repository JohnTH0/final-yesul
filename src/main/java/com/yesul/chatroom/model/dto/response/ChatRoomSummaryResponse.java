package com.yesul.chatroom.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "채팅방 목록 상세 조회 DTO")
public class ChatRoomSummaryResponse {

    private Long roomId;
    private String lastMessage;
    private int adminUnreadCount;

    private Long userId;
    private String userName;
    private String userProfileUrl;

}

