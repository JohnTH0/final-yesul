package com.yesul.exception.handler;

public class ChatRoomNotFoundException extends RuntimeException {
  public ChatRoomNotFoundException(String message) {
    super(message);
  }
}
