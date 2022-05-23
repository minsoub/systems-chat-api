package com.bithumbsystems.persistence.mongodb.message.service;

import com.bithumbsystems.persistence.mongodb.message.model.entity.ChatMessage;
import com.bithumbsystems.persistence.mongodb.message.repository.ChatMessageRepository;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonTimestamp;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageDomainService {

    private final ChatMessageRepository chatMessageRepository;

    public Flux<ChatMessage> saveAll(Flux<ChatMessage> chatMessages) {
        return chatMessageRepository.saveAll(chatMessages);
    }

    public Flux<ChatMessage> changeStream(final Mono<Set<UUID>> chats, final BsonTimestamp bsonTimestamp) {
        return chatMessageRepository.changeStream(bsonTimestamp)
            .filterWhen(message -> chats.map(chatIds -> chatIds.contains(message.getProjectId())));
    }
}
