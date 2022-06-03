package com.bithumbsystems.persistence.mongodb.message.repository;

import com.bithumbsystems.persistence.mongodb.message.model.entity.ChatResumeToken;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ChatResumeTokenRepository extends ReactiveMongoRepository<ChatResumeToken, String> {
    Mono<ChatResumeToken> findBySiteIdAndChatRoom(String siteId, String chatRoom);
}
