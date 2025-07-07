package com.yesul.chatroom.service;

public interface TempMessageService {

    void saveTempMessage(Long chatRoomId, Long userId, String message);

    String getTempMessage(Long chatRoomId, Long userId);

    void deleteTempMessage(Long chatRoomId, Long userId);
}
