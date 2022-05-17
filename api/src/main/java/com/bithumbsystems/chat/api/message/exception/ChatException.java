package com.bithumbsystems.chat.api.message.exception;

import com.bithumbsystems.chat.api.core.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ChatException extends RuntimeException {
  public ChatException(ErrorCode errorCode) {
    super(String.valueOf(errorCode));
  }
}