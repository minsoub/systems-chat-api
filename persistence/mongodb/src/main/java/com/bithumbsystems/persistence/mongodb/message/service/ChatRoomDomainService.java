package com.bithumbsystems.persistence.mongodb.message.service;

import com.bithumbsystems.persistence.mongodb.message.model.entity.ChatRoom;
import com.bithumbsystems.persistence.mongodb.message.model.enums.Role;
import com.bithumbsystems.persistence.mongodb.message.repository.ChatRoomRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomDomainService {

    private final ChatRoomRepository chatRoomRepository;

    public Flux<ChatRoom> findByAccountIdAndRole(UUID accountId, Role role) {
        return chatRoomRepository.findByAccountIdAndRole(accountId, role);
    }

    public Mono<ChatRoom> save(ChatRoom chatRoom) {
        return chatRoomRepository.save(chatRoom);
    }

    public Mono<ChatRoom> findByAccountIdAndRoleAndProjectID(UUID accountId, Role role, UUID projectId) {
        return chatRoomRepository.findByAccountIdAndRoleAndProjectsContains(accountId, role, projectId);
    }
}
