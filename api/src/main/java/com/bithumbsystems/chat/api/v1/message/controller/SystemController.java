package com.bithumbsystems.chat.api.v1.message.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SystemController {

  @MessageMapping("/health")
  public String health() {
    log.info("Creating new message");
    return "OK";
  }
}
