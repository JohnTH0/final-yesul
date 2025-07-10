package com.yesul.chatroom.controller.admin;

import com.yesul.chatroom.model.dto.AdminChatRoomsResponse;
import com.yesul.chatroom.model.dto.ChatRoomSummaryResponse;
import com.yesul.chatroom.model.entity.Message;
import com.yesul.chatroom.model.entity.enums.Type;
import com.yesul.chatroom.service.ChatRoomService;
import com.yesul.chatroom.service.MessageService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/admin/chatroom")
@RequiredArgsConstructor
public class AdminChatRoomController {

    private final ChatRoomService chatRoomService;
    private final MessageService messageService;

    @GetMapping
    public String getAdminChatRooms(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "8") int size,
            Model model
    ) {
        AdminChatRoomsResponse response = chatRoomService.getAdminChatRooms(cursor, size);

        model.addAttribute("chatRooms", response.getChatRooms());
        model.addAttribute("totalUnreadCount", response.getTotalUnreadCount());
        model.addAttribute("nextCursor", response.getNextCursor());
        System.out.println(response.getTotalUnreadCount());

        return "admin/admin-chat";
    }


    @GetMapping("/{roomId}")
    public String getAdminChatRoom(
            @PathVariable Long roomId,
            Model model
    ) {
        int size = 20;

        Slice<Message> messageSlice = messageService.getMessagesWithCursor(roomId, null, size, Type.ADMIN);

        List<Message> messages = new ArrayList<>(messageSlice.getContent());
        Collections.reverse(messages);

        Long lastMessageId = !messages.isEmpty()
                ? messages.get(messages.size() - 1).getId()
                : null;

        model.addAttribute("roomId", roomId);
        model.addAttribute("messages", messages);
        model.addAttribute("hasNext", messageSlice.hasNext());
        model.addAttribute("lastMessageId", lastMessageId);

        return "admin/admin-chat";
    }




    // 2) 무한스크롤용 메시지 JSON API
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<Map<String, Object>> getMoreMessages(
            @PathVariable Long roomId,
            @RequestParam Long cursor
    ) {
        int size = 20;

        Slice<Message> messageSlice = messageService.getMessagesWithCursor(roomId, cursor, size, Type.ADMIN);
        List<Message> messages = new ArrayList<>(messageSlice.getContent());
        Collections.reverse(messages);

        Long lastMessageId = !messages.isEmpty() ? messages.get(messages.size() - 1).getId() : null;

        List<Map<String, Object>> messageDtos = new ArrayList<>();
        for (Message msg : messages) {
            Map<String, Object> dto = new HashMap<>();
            dto.put("id", msg.getId());
            dto.put("senderType", msg.getSenderType().name());
            dto.put("messageType", msg.getMessageType().name());
            dto.put("messageContext", msg.getMessageContext());
            dto.put("createdAt", msg.getCreatedAt());
            messageDtos.add(dto);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("messages", messageDtos);
        response.put("lastMessageId", lastMessageId);
        response.put("hasNext", messageSlice.hasNext());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public String searchChatRoom(
            @RequestParam String keyword,
            Model model
    ) {
        List<ChatRoomSummaryResponse> chatRooms = chatRoomService.searchChatRoom(keyword);
        model.addAttribute("chatRooms", chatRooms);
        return "admin/admin-chat :: searchResultFragment";
    }
}
