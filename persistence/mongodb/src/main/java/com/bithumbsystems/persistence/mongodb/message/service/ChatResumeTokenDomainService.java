package com.bithumbsystems.persistence.mongodb.message.service;

import com.bithumbsystems.persistence.mongodb.message.model.entity.ChatResumeToken;
import com.bithumbsystems.persistence.mongodb.message.repository.ChatResumeTokenRepository;
import java.time.Instant;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonTimestamp;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatResumeTokenDomainService {

    private final ChatResumeTokenRepository userResumeTokenRepository;

    public void saveAndGenerateNewTokenFor(final String siteId, final String chatRoom) {
        log.debug("Saving token for {} {}", siteId, chatRoom);
        final var userToken = userResumeTokenRepository.findBySiteIdAndChatRoom(siteId, chatRoom)
                .defaultIfEmpty(new ChatResumeToken(siteId, chatRoom))
                .map(changeCurrentToken());
        userResumeTokenRepository.saveAll(userToken)
                .subscribeOn(Schedulers.boundedElastic())
                .doOnComplete(() -> log.debug("Saving token for {} {}", siteId, chatRoom))
                .subscribe();
    }

    private Function<ChatResumeToken, ChatResumeToken> changeCurrentToken() {
        return userResumeToken -> {
            final long epochSecond = Instant.now().getEpochSecond();
            log.debug("changeCurrentToken {}", epochSecond);
            userResumeToken.setTokenTimestamp(new BsonTimestamp((int) epochSecond, 0));
            return userResumeToken;
        };
    }

    public Mono<BsonTimestamp> getResumeTimestamp(final String siteId, final String chatRoom) {
        return userResumeTokenRepository.findBySiteIdAndChatRoom(siteId, chatRoom)
                .map(ChatResumeToken::getTokenTimestamp)
                .defaultIfEmpty(new BsonTimestamp((int) Instant.now().getEpochSecond(), 0));
    }
}
