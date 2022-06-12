package com.bithumbsystems.chat.api.v1.message.service;

import com.bithumbsystems.chat.api.v1.message.model.Account;
import com.bithumbsystems.chat.api.v1.message.model.mapper.MessageMapper;
import com.bithumbsystems.chat.api.v1.message.model.request.MessageRequest;
import com.bithumbsystems.chat.api.v1.message.model.response.ChatMessageResponse;
import com.bithumbsystems.persistence.mongodb.message.model.entity.ChatChannel;
import com.bithumbsystems.persistence.mongodb.message.model.entity.ChatMessage;
import com.bithumbsystems.persistence.mongodb.message.model.enums.Role;
import com.bithumbsystems.persistence.mongodb.message.service.ChatChannelDomainService;
import com.bithumbsystems.persistence.mongodb.message.service.ChatMessageDomainService;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public
class ChatService {

    private final ChatChannelDomainService chatChannelDomainService;
    private final ChatMessageDomainService chatMessageDomainService;

    public Flux<ChatMessageResponse> connectChatRoom(final Account account, final String chatRoom, final String siteId) {
        return chatChannelDomainService.findByAccountIdAndRoleAndChatRoomsContains(account.getAccountId(), account.getRole(), chatRoom)
            .as(chatChannel -> chatMessageDomainService.findMessages(chatRoom, siteId))
            .defaultIfEmpty(new ChatMessage())
            .flatMap(chatMessage -> Mono.just(MessageMapper.INSTANCE.chatMessageToChatMessageResponse(chatMessage)));
    }

    public Mono<ChatChannel> createChatRoom(final Account account, final String chatRoom, final String siteId) {
        ChatChannel chatChannel = new ChatChannel(account.getAccountId(), account.getRole(), new HashSet<>(), siteId);
        chatChannel.addChatRoom(chatRoom);
        return chatChannelDomainService.save(chatChannel);
    }

    public Mono<Set<String>> getChatRooms(final String accountId, final Role role, final String siteId) {
        return chatChannelDomainService.findByAccountIdAndRoleAndSiteId(accountId, role, siteId)
            .doOnNext(chatChannel -> log.info("found user {} {}", chatChannel.getAccountId(), chatChannel.getRole()))
            .flatMapIterable(ChatChannel::getChatRooms)
            .collect(Collectors.toSet())
            .doOnNext(uuids -> log.info("set size {}", uuids.size()))
            .defaultIfEmpty(Set.of())
            .doOnNext(uuids -> log.info("set size {}", uuids.size()));
    }

    public Flux<ChatMessage> saveMessage(final Flux<MessageRequest> chatMessageRequests, final Account account) {
        return chatMessageRequests.flatMap(chatMessageRequest -> saveMessage(chatMessageRequest, account));
    }

    public Mono<ChatMessage> saveMessage(final MessageRequest chatMessageRequest, final Account account) {
        final var chatMessage = MessageMapper.INSTANCE.messageRequestToChatMessage(chatMessageRequest);
        chatMessage.setAccountId(account.getAccountId());
        chatMessage.setRole(account.getRole());
        return chatMessageDomainService.save(chatMessage);
    }
}
