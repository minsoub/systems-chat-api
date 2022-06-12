package com.bithumbsystems.persistence.mongodb.message.repository;

import static com.mongodb.client.model.changestream.OperationType.INSERT;

import com.bithumbsystems.persistence.mongodb.message.model.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonTimestamp;
import org.springframework.data.mongodb.core.ChangeStreamEvent;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageCustomRepository {

  private final ReactiveMongoTemplate reactiveMongoTemplate;

  @Override
  public Flux<ChatMessage> changeStream(final BsonTimestamp bsonTimestamp) {
      return reactiveMongoTemplate.changeStream(ChatMessage.class)
          .watchCollection(ChatMessage.class)
//          .resumeAt(bsonTimestamp)
          .listen()
          .doOnNext(e -> log.info("event " + e))
          .filter(event -> event.getOperationType() == INSERT)
          .mapNotNull(ChangeStreamEvent::getBody);
  }

}
