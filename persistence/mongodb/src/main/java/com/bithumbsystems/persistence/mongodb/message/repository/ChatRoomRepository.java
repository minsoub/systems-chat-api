package com.bithumbsystems.persistence.mongodb.message.repository;

import com.bithumbsystems.persistence.mongodb.message.model.entity.ChatRoom;
import com.bithumbsystems.persistence.mongodb.message.model.enums.Role;
import java.util.UUID;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ChatRoomRepository extends ReactiveMongoRepository<ChatRoom, UUID> {
  Flux<ChatRoom> findByAccountIdAndRole(UUID accountId, Role role);

  Mono<ChatRoom> findByAccountIdAndRoleAndProjectsContains(UUID accountId, Role role, UUID projectId);
}
