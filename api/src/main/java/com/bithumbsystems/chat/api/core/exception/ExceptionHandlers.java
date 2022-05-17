package com.bithumbsystems.chat.api.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import reactor.core.publisher.Mono;

@Slf4j
public class ExceptionHandlers {

  @MessageExceptionHandler(Exception.class)
  public Mono<String> serverExceptionHandler(Exception ex) {
    log.error(ex.getMessage(), ex);
    return Mono.just(ex.getMessage());
  }
}