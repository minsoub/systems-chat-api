package com.bithumbsystems.persistence.mongodb.message.repository;

import com.bithumbsystems.persistence.mongodb.message.model.UserResumeTokenDocument;
import java.util.UUID;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserResumeTokenRepository extends ReactiveMongoRepository<UserResumeTokenDocument, UUID> {
    Mono<UserResumeTokenDocument> findByUserName(String username);
    Mono<Long> deleteByUserName(String username);
}
