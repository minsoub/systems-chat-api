package com.bithumbsystems.persistence.mongodb.message.repository;

import com.bithumbsystems.persistence.mongodb.message.model.entity.ChatMessage;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ChatMessageRepository extends ReactiveMongoRepository<ChatMessage, String>, ChatMessageCustomRepository {

  Flux<ChatMessage> findAllByChatRoomAndSiteIdAndIsDeleteFalse(String chatRoom, String siteId);
}
