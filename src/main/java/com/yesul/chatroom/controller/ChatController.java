package com.yesul.chatroom.controller;

import com.yesul.chatroom.model.dto.ChatRoomResult;
import com.yesul.chatroom.model.entity.Message;
import com.yesul.chatroom.service.ChatRoomService;
import com.yesul.chatroom.service.MessageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    @PostMapping
    public String createOrFindChatRoom(@RequestParam Long userId) {
        ChatRoomResult result = chatRoomService.findOrCreateRoom(userId);
        return "redirect:/users/chatroom/" + result.getChatRoom().getId() + "?new=" + result.isNewlyCreated();
    }

    //채팅방 조회(메시지 조회, 무한 스크롤 구현)
    @GetMapping("/{roomId}")
    public String getChatRoom(
            @PathVariable Long roomId,
            @RequestParam(name = "new", required = false) Boolean isNew,
            Model model
    ) {
        Slice<Message> messageSlice;

        if (Boolean.TRUE.equals(isNew)) {
            messageSlice = new SliceImpl<>(List.of()); // 빈 Slice
        } else {
            messageSlice = messageService.getMessagesWithCursor(roomId, null, 5);
        }

        List<Message> messages = messageSlice.getContent();

        model.addAttribute("roomId", roomId);
        model.addAttribute("messages", messages);
        model.addAttribute("hasNext", messageSlice.hasNext());

        if (!messages.isEmpty()) {
            Long lastMessageId = messages.get(messages.size() - 1).getId();
            model.addAttribute("lastMessageId", lastMessageId);
        }

        return "user-chat";
    }






}
