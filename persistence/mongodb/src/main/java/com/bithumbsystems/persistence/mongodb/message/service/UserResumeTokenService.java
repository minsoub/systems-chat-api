package com.bithumbsystems.persistence.mongodb.message.service;

import com.bithumbsystems.persistence.mongodb.message.model.entity.UserResumeToken;
import com.bithumbsystems.persistence.mongodb.message.model.enums.Role;
import com.bithumbsystems.persistence.mongodb.message.repository.UserResumeTokenRepository;
import java.time.Clock;
import java.util.UUID;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonTimestamp;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
public class UserResumeTokenService {

    private final UserResumeTokenRepository userResumeTokenRepository;
    private final Clock clock;

    UserResumeTokenService(final UserResumeTokenRepository userResumeTokenRepository, final Clock clock) {
        this.userResumeTokenRepository = userResumeTokenRepository;
        this.clock = clock;
    }

    public void saveAndGenerateNewTokenFor(final UUID accountId, final Role role) {
        log.info("Saving token for user {}", accountId);
        final var userToken = userResumeTokenRepository.findByAccountIdAndRole(accountId, role)
                .defaultIfEmpty(new UserResumeToken(accountId, role))
                .map(changeCurrentToken());
        userResumeTokenRepository.saveAll(userToken)
                .subscribeOn(Schedulers.boundedElastic())
                .doOnComplete(() -> log.info("User {} token saved ", accountId))
                .subscribe();
    }

    private Function<UserResumeToken, UserResumeToken> changeCurrentToken() {
        return userResumeToken -> {
            final long epochSecond = clock.instant().getEpochSecond();
            userResumeToken.setTokenTimestamp(new BsonTimestamp((int) epochSecond, 0));
            return userResumeToken;
        };
    }

    public Mono<BsonTimestamp> getResumeTimestamp(final UUID accountId, final Role role) {
        return userResumeTokenRepository.findByAccountIdAndRole(accountId, role)
                .map(UserResumeToken::getTokenTimestamp)
                .defaultIfEmpty(new BsonTimestamp((int) clock.instant().getEpochSecond(), 0));
    }
}
