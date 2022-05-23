package com.bithumbsystems.chat.api.v1.message.controller;

import com.bithumbsystems.chat.api.core.model.response.SingleResponse;
import com.bithumbsystems.chat.api.v1.message.model.Account;
import com.bithumbsystems.chat.api.v1.message.model.request.MessageRequest;
import com.bithumbsystems.chat.api.v1.message.model.response.ChatCreatedResponse;
import com.bithumbsystems.chat.api.v1.message.service.ChatService;
import com.bithumbsystems.chat.api.v1.message.service.ChatWatcherService;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@Slf4j
@RequiredArgsConstructor
class MessageController {

    private final ChatService chatService;
    private final ChatWatcherService chatWatcherService;

    @MessageMapping("message")
    @Deprecated
    public String test(final String message) {
        log.info("Creating new message");
        return message;
    }

    @MessageMapping("join-chat")
    public Mono<SingleResponse<ChatCreatedResponse>> joinChat(@AuthenticationPrincipal final Account account) {
        log.info("Creating join Chat");
        log.info("account: {}", account.toString());

        final UUID chatId = UUID.randomUUID();
        return chatService.connectChatRoom(account)
                .log()
                .map(ignored -> new SingleResponse<>(new ChatCreatedResponse(chatId)));
    }

    @MessageMapping("get-chat-rooms")
    public Mono<Set<UUID>> getUserChats(@AuthenticationPrincipal final Account account) {
        return chatService.getChatRooms(account.getAccountId(), account.getRole());
    }

    @MessageMapping("send-chat-message")
    public Flux<MessageRequest> sandMessages(final Flux<MessageRequest> messageRequests, @AuthenticationPrincipal final Account account) {
        final var incomingMessagesSubscription = chatService.saveMessages(messageRequests);
        final var chatRooms = chatService.getChatRooms(account.getAccountId(), account.getRole());
        return chatWatcherService.sandMessages(chatRooms, account.getAccountId(), account.getRole())
                .doOnNext(messageRequest -> log.info("Message reply {}", messageRequest))
                .doOnSubscribe(subscription -> log.info("Subscribing to watcher : " + account.getAccountId()))
                .doOnCancel(() -> {
                    log.info("Cancelled");
                    incomingMessagesSubscription.dispose();
                })
                .doOnError(throwable -> log.error(throwable.getMessage()));
    }
}
