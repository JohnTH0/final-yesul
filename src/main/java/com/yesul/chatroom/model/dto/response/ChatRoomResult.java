package com.yesul.chatroom.model.dto.response;

import com.yesul.chatroom.model.entity.ChatRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "채팅방 응답 DTO")
public class ChatRoomResult {

    private ChatRoom chatRoom;
    private boolean newlyCreated;
}
