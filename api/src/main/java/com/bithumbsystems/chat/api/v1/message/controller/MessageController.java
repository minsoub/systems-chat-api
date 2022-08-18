package com.bithumbsystems.chat.api.v1.message.controller;

import com.bithumbsystems.chat.api.v1.message.model.request.ChannelRequest;
import com.bithumbsystems.chat.api.v1.message.model.request.JoinChatRequest;
import com.bithumbsystems.chat.api.v1.message.model.request.MessageRequest;
import com.bithumbsystems.chat.api.v1.message.model.response.ChatMessageResponse;
import com.bithumbsystems.chat.api.v1.message.model.response.MessageResponse;
import com.bithumbsystems.chat.api.v1.message.service.ChatService;
import com.bithumbsystems.chat.api.v1.message.service.ChatWatcherService;
import com.bithumbsystems.persistence.mongodb.message.model.Account;
import com.bithumbsystems.persistence.mongodb.message.model.entity.ChatChannel;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
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

    @MessageMapping("/")
    public String health() {
        log.debug("Creating new message");
        return "OK";
    }

    @MessageMapping("create-chat")
    public Mono<ChatChannel> createChat(final JoinChatRequest joinChatRequest, @AuthenticationPrincipal final Account account) {
        log.debug("Create Chat");
        return chatService.createChatRoom(account, joinChatRequest.getChatRoom(), joinChatRequest.getSiteId());
    }

    @MessageMapping("join-chat")
    public Mono<List<ChatMessageResponse>> joinChat(final JoinChatRequest joinChatRequest, @AuthenticationPrincipal final Account account) {
        log.debug("Join Chat");
        return chatService.connectChatRoom(account, joinChatRequest.getChatRoom(), joinChatRequest.getSiteId())
            .collectSortedList(Comparator.comparing(ChatMessageResponse::getCreateDate));
    }

    @MessageMapping("get-chat-rooms")
    public Mono<Set<String>> getUserChats(final String siteId, @AuthenticationPrincipal final Account account) {
        return chatService.getChatRooms(account, siteId);
    }

    @MessageMapping("channel-chat-message")
    public Flux<MessageResponse> sendMessagesChannel(final Flux<ChannelRequest> channelRequests, @AuthenticationPrincipal final Account account) {
        return channelRequests.flatMap(channelRequest -> chatWatcherService.channelMessages(channelRequest)
//            .doOnNext(request -> log.info("Message reply {}", request))
//            .doOnSubscribe(subscription -> log.info("Subscribing to watcher : " + account.getAccountId()))
            .doOnError(throwable -> log.error(throwable.getMessage())));
    }

    @MessageMapping("send-chat-message")
    public Mono<ChatMessageResponse> sendMessages(final MessageRequest messageRequest, @AuthenticationPrincipal final Account account) {
        return chatService.saveMessage(messageRequest, account);
    }
}
