package com.bithumbsystems.persistence.mongodb.message.repository;

import com.bithumbsystems.persistence.mongodb.message.model.entity.ChatMessage;
import java.util.UUID;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends ReactiveMongoRepository<ChatMessage, UUID>, ChatMessageCustomRepository {
}
