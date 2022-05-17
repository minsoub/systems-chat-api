package com.bithumbsystems.chat.api.message.controller;

import com.bithumbsystems.chat.api.message.service.ChatToUserMappingsHolder;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
class MessageReadController {

    private final ChatToUserMappingsHolder chatToUserMappingsHolder;

    @MessageMapping("get-user-chats")
    public Mono<Set<UUID>> getUserChats(@AuthenticationPrincipal final UserDetails user) {
        return chatToUserMappingsHolder.getUserChatRooms(user.getUsername());
    }
}

