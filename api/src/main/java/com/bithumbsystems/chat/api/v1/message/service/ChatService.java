package com.bithumbsystems.chat.api.v1.message.service;

import com.bithumbsystems.chat.api.v1.message.model.Account;
import com.bithumbsystems.chat.api.v1.message.model.mapper.MessageMapper;
import com.bithumbsystems.chat.api.v1.message.model.request.MessageRequest;
import com.bithumbsystems.persistence.mongodb.message.model.entity.ChatMessage;
import com.bithumbsystems.persistence.mongodb.message.model.entity.ChatRoom;
import com.bithumbsystems.persistence.mongodb.message.model.enums.Role;
import com.bithumbsystems.persistence.mongodb.message.service.ChatMessageDomainService;
import com.bithumbsystems.persistence.mongodb.message.service.ChatRoomDomainService;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public
class ChatService {

    private final ChatRoomDomainService chatRoomDomainService;
    private final ChatMessageDomainService chatMessageDomainService;
    private final MessageMapper messageMapper;

    public Mono<Boolean> connectChatRoom(Account account) {
       return chatRoomDomainService.findByAccountIdAndRoleAndProjectID(account.getAccountId(), account.getRole(), account.getProjectId())
                .defaultIfEmpty(new ChatRoom(account.getAccountId(), account.getRole(), new HashSet<>()))
                .map(chatRoom -> {
                    chatRoom.addChat(account.getProjectId());
                    return chatRoom;
                })
               .map(chatRoomDomainService::save)
               .doOnNext(chatRoom -> log.info("saving document {}", chatRoom))
               .map(chatRoom -> true);
    }

    public Mono<Set<UUID>> getChatRooms(final UUID accountId, final Role role) {
        return chatRoomDomainService.findByAccountIdAndRole(accountId, role)
                .doOnNext(chatRoom -> log.info("found user {} {}", chatRoom.getAccountId(), chatRoom.getRole()))
                .flatMapIterable(ChatRoom::getProjects)
                .collect(Collectors.toSet())
                .doOnNext(uuids -> log.info("set size {}", uuids.size()))
                .defaultIfEmpty(Set.of())
                .doOnNext(uuids -> log.info("set size {}", uuids.size()));
    }

    public Disposable saveMessages(Flux<MessageRequest> chatMessageRequests) {
        final var chatMessages = chatMessageRequests.map(messageMapper::messageRequestToChatMessage);
        return chatMessageDomainService.saveAll(chatMessages)
          .then()
          .subscribeOn(Schedulers.boundedElastic())
          .doOnSubscribe(subscription -> log.info("subscribing to user input channel"))
          .subscribe();
    }
}
