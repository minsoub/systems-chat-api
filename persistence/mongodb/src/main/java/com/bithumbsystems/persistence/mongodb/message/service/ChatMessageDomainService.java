package com.bithumbsystems.persistence.mongodb.message.service;

import com.bithumbsystems.persistence.mongodb.message.model.entity.ChatMessage;
import com.bithumbsystems.persistence.mongodb.message.repository.ChatMessageRepository;
import java.time.LocalDateTime;
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

    public Mono<ChatMessage> save(ChatMessage chatMessage) {
        chatMessage.setCreateDate(LocalDateTime.now());
        chatMessage.setId(UUID.randomUUID().toString());
        return chatMessageRepository.save(chatMessage);
    }

    public Flux<ChatMessage> changeStream(final String chatRoom, final String siteId, final BsonTimestamp bsonTimestamp) {
        return chatMessageRepository.changeStream(bsonTimestamp)
            .filterWhen(message -> Mono.just(message.getChatRoom().equals(chatRoom) && message.getSiteId().equals(siteId)));
    }

    public Flux<ChatMessage> findMessages(final String chatRoom, final String siteId) {
        return chatMessageRepository.findAllByChatRoomAndSiteIdAndIsDeleteFalse(chatRoom, siteId);
    }
}
