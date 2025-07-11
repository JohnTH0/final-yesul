package com.yesul.chatroom.controller.user;

import com.yesul.chatroom.model.dto.response.ChatRoomResult;
import com.yesul.chatroom.model.entity.Message;
import com.yesul.chatroom.model.entity.enums.Type;
import com.yesul.chatroom.service.ChatRoomService;
import com.yesul.chatroom.service.MessageService;
import com.yesul.user.service.PrincipalDetails;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/users/chatroom")
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomService chatRoomService;
    private final MessageService messageService;

    // 채팅방 생성 or 재사용
    @GetMapping
    public String createOrFindChatRoom(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam Long adminId
    ) {
        ChatRoomResult result = chatRoomService.findOrCreateRoom(principalDetails.getUser().getId(),adminId);
        return "redirect:/users/chatroom/" + result.getChatRoom().getId() + "?new=" + result.isNewlyCreated();
    }

    // 채팅방 조회(메시지 조회, 무한 스크롤 구현)
    @GetMapping("/{roomId}")
    public String getChatRoom(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long roomId,
            @RequestParam(name = "new", required = false) Boolean isNew,
            Model model
    ) {
        Slice<Message> messageSlice;

        int size = 20; // 최초 로딩 메시지 수

        if (Boolean.TRUE.equals(isNew)) {
            messageSlice = new SliceImpl<>(List.of()); // 새로 만든 방이면 빈 메시지
        } else {
            // 커서 조건 null → Service에서 알아서 분기
            messageSlice = messageService.getMessagesWithCursor(roomId, null, size, Type.USER);
        }

        List<Message> messages = new ArrayList<>(messageSlice.getContent());
        Collections.reverse(messages);
        model.addAttribute("roomId", roomId);
        model.addAttribute("messages", messages);
        model.addAttribute("hasNext", messageSlice.hasNext());

        Long lastMessageId = !messages.isEmpty()
                ? messages.get(messages.size() - 1).getId()
                : null;

        model.addAttribute("lastMessageId", lastMessageId);

        return "user/user-chat";
    }
}
