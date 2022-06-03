package com.bithumbsystems.persistence.mongodb.message.service;

import com.bithumbsystems.persistence.mongodb.message.model.entity.ChatChannel;
import com.bithumbsystems.persistence.mongodb.message.model.enums.Role;
import com.bithumbsystems.persistence.mongodb.message.repository.ChatChannelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatChannelDomainService {

    private final ChatChannelRepository chatChannelrepository;

    public Flux<ChatChannel> findByAccountIdAndRoleAndSiteId(String accountId, Role role, String siteId) {
        return chatChannelrepository.findByAccountIdAndRoleAndSiteId(accountId, role, siteId);
    }

    public Mono<ChatChannel> save(ChatChannel chatChannel) {
        return chatChannelrepository.save(chatChannel);
    }

    public Mono<ChatChannel> findByAccountIdAndRoleAndChatRoomsContains(String accountId, Role role, String chatRoom) {
        return chatChannelrepository.findByAccountIdAndRoleAndChatRoomsContains(accountId, role, chatRoom);
    }
}
