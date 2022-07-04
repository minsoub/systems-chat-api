package com.bithumbsystems.chat.api.v1.message.model.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ErrorCode {
  INVALID_CHAT_ROOM("CHAT_ROOM is invalid"),
  INVALID_TOKEN("token is invalid"),
  INVALID_FILE("file is invalid"),
  FAIL_SAVE_FILE("file save fail");

  private final String message;
}
