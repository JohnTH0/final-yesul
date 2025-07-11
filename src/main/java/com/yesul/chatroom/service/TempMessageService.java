package com.yesul.chatroom.service;

public interface TempMessageService {

    void saveTempMessage(Long chatRoomId, Long userId, String message, int dbIndex);

    String getTempMessage(Long chatRoomId, Long userId, int dbIndex);

    void deleteTempMessage(Long chatRoomId, Long userId, int dbIndex);
}
