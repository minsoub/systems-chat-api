package com.bithumbsystems.persistence.mongodb.message.repository;

import com.bithumbsystems.persistence.mongodb.message.model.MessageDocument;
import java.util.UUID;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends ReactiveMongoRepository<MessageDocument, UUID> {
}
