package com.bithumbsystems.persistence.mongodb.message.repository;

import com.bithumbsystems.persistence.mongodb.message.model.entity.UserResumeToken;
import com.bithumbsystems.persistence.mongodb.message.model.enums.Role;
import java.util.UUID;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserResumeTokenRepository extends ReactiveMongoRepository<UserResumeToken, UUID> {
    Mono<UserResumeToken> findByAccountIdAndRole(UUID accountId, Role role);
}
