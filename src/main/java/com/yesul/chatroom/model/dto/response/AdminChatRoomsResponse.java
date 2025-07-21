package com.yesul.chatroom.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "관리자 채팅방 목록 응답 DTO")
public class AdminChatRoomsResponse {

    private List<ChatRoomSummaryResponse> chatRooms;
    private int totalUnreadCount;
    private Long nextCursor;
}
