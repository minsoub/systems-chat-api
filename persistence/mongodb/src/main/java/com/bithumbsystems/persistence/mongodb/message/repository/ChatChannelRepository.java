package com.bithumbsystems.persistence.mongodb.message.repository;

import com.bithumbsystems.persistence.mongodb.message.model.entity.ChatChannel;
import com.bithumbsystems.persistence.mongodb.message.model.enums.Role;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ChatChannelRepository extends ReactiveMongoRepository<ChatChannel, String> {
  Flux<ChatChannel> findByAccountIdAndRoleAndSiteId(String accountId, Role role, String siteId);

  Mono<ChatChannel> findByAccountIdAndRoleAndChatRoomsContains(String accountId, Role role, String projectId);
}
