package com.bithumbsystems.chat.api.core.exception;

import com.bithumbsystems.chat.api.v1.message.exception.ChatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import reactor.core.publisher.Mono;

@Slf4j
@ControllerAdvice
public class ExceptionHandlers {

  @MessageExceptionHandler(Exception.class)
  public Mono<String> serverExceptionHandler(Exception ex) {
    log.error(ex.getMessage(), ex);
    return Mono.just(ex.getMessage());
  }

  @MessageExceptionHandler(ChatException.class)
  public Mono<String> serverChatExceptionHandler(ChatException ex) {
    log.error(ex.getMessage(), ex);
    return Mono.just(ex.getMessage());
  }
}