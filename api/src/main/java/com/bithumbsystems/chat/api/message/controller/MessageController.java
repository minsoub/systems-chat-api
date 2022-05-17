package com.bithumbsystems.chat.api.message.controller;

import com.bithumbsystems.chat.api.message.model.Message;
import com.bithumbsystems.chat.api.message.model.request.JoinChatRequest;
import com.bithumbsystems.chat.api.message.model.response.ChatCreatedResponse;
import com.bithumbsystems.chat.api.message.service.ChatToUserMappingsHolder;
import com.bithumbsystems.chat.api.message.service.NewMessageWatcher;
import com.bithumbsystems.persistence.mongodb.message.model.MessageDocument;
import com.bithumbsystems.persistence.mongodb.message.repository.MessageRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Controller
@Slf4j
@RequiredArgsConstructor
class MessageController {

    private final ChatToUserMappingsHolder chatRoomUserMappings;
    private final MessageRepository messageRepository;
    private final NewMessageWatcher newMessageWatcher;

    @MessageMapping("message")
    public String test(final String message) {
        log.info("Creating new message");
        return message;
    }

    @MessageMapping("create-chat")
    public Mono<ChatCreatedResponse> createChat(final String join, @AuthenticationPrincipal final UserDetails user) {
        log.info("Creating new chat");
        final UUID chatId = UUID.randomUUID();
        return chatRoomUserMappings.putUserToChat(user.getUsername(), chatId)
                .log()
                .map(ignored -> new ChatCreatedResponse(chatId));
    }

    @MessageMapping("join-chat")
    public Mono<Boolean> joinChat(final JoinChatRequest joinChatRequest, @AuthenticationPrincipal final UserDetails user) {
        return chatRoomUserMappings.putUserToChat(user.getUsername(), joinChatRequest.getChatId()).log();
    }

    @MessageMapping("chat-channel")
    public Flux<Message> handle(final Flux<Message> incomingMessages, @AuthenticationPrincipal final UserDetails user) {
        final var messages = incomingMessages.map(this::toMessageDocument);
        final var incomingMessagesSubscription = messageRepository.saveAll(messages)
                .then()
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(subscription -> log.info("subscribing to user " + user.getUsername() + " input channel"))
                .subscribe();
        final var userChats = chatRoomUserMappings.getUserChatRooms(user.getUsername());
        return newMessageWatcher.newMessagesForChats(userChats, user.getUsername())
                .doOnNext(message -> log.info("Message reply {}", message))
                .doOnSubscribe(subscription -> log.info("Subscribing to watcher : " + user.getUsername()))
                .doOnCancel(() -> {
                    log.info("Cancelled");
                    incomingMessagesSubscription.dispose();
                })
                .doOnError(throwable -> log.error(throwable.getMessage()));
    }

    private MessageDocument toMessageDocument(final Message message) {
        return new MessageDocument(
                message.getUsernameFrom(),
                message.getContent(),
                message.getChatRoomId()
        );
    }
}
