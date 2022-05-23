package com.bithumbsystems.persistence.mongodb.message.repository;

import com.bithumbsystems.persistence.mongodb.message.model.entity.ChatMessage;
import org.bson.BsonTimestamp;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ChatMessageCustomRepository {

  Flux<ChatMessage> changeStream(BsonTimestamp bsonTimestamp);
}
