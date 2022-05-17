package com.bithumbsystems.chat.api.message.service;

import static com.mongodb.client.model.changestream.OperationType.INSERT;

import com.bithumbsystems.chat.api.message.model.Message;
import com.bithumbsystems.persistence.mongodb.message.model.MessageDocument;
import com.bithumbsystems.persistence.mongodb.message.service.UserResumeTokenService;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonTimestamp;
import org.reactivestreams.Publisher;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class NewMessageWatcher {

    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final UserResumeTokenService resumeTokenService;

    public Flux<Message> newMessagesForChats(final Mono<Set<UUID>> chats, final String username) {
        return resumeTokenService.getResumeTimestampFor(username)
                .flatMapMany(bsonTimestamp -> changeStream(username, chats, bsonTimestamp))
                .doOnCancel(() -> resumeTokenService.saveAndGenerateNewTokenFor(username));
    }

    private Flux<Message> changeStream(final String username,
                                       final Mono<Set<UUID>> chats,
                                       final BsonTimestamp bsonTimestamp) {
        final Function<MessageDocument, Publisher<Boolean>> messageIsForThisUserChat =
                message -> chats.map(chatIds -> chatIds.contains(message.getChatRoomId()));
        return reactiveMongoTemplate.changeStream(MessageDocument.class)
                .watchCollection("messages")
                .resumeAt(bsonTimestamp)
                .listen()
                .doOnNext(e -> log.info("event " + e))
                .filter(event -> event.getOperationType() == INSERT)
                .map(ChangeStreamEvent::getBody)
                .filter(m -> m.isNotFromUser(username))
                .filterWhen(messageIsForThisUserChat)
                .map(messageDocument -> new Message(messageDocument.getUsernameFrom(), messageDocument.getContent(), messageDocument.getChatRoomId()));
    }
}
