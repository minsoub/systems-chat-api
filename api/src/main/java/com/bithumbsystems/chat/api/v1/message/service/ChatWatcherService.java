package com.bithumbsystems.chat.api.v1.message.service;

import com.bithumbsystems.chat.api.v1.message.model.request.MessageRequest;
import com.bithumbsystems.persistence.mongodb.message.model.enums.Role;
import com.bithumbsystems.persistence.mongodb.message.service.ChatMessageDomainService;
import com.bithumbsystems.persistence.mongodb.message.service.UserResumeTokenService;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonTimestamp;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatWatcherService {

    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final UserResumeTokenService resumeTokenService;
    private final ChatMessageDomainService chatMessageDomainService;

    public Flux<MessageRequest> sandMessages(Mono<Set<UUID>> chatRooms, UUID accountId, Role role) {
        return resumeTokenService.getResumeTimestamp(accountId, role)
            .flatMapMany(bsonTimestamp -> changeStream(chatRooms, bsonTimestamp))
            .doOnCancel(() -> resumeTokenService.saveAndGenerateNewTokenFor(accountId, role));
    }

    private Flux<MessageRequest> changeStream(final Mono<Set<UUID>> chats, final BsonTimestamp bsonTimestamp) {
        return chatMessageDomainService.changeStream(chats, bsonTimestamp)
            .map(chatMessage -> new MessageRequest(chatMessage.getAccountId(), chatMessage.getRole(), chatMessage.getContent(), chatMessage.getProjectId()));
    }
}
