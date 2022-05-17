package com.bithumbsystems.chat.api.message.service;

import java.util.Set;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface ChatToUserMappingsHolder {
    Mono<Boolean> putUserToChat(String userName, UUID chatId);

    Mono<Set<UUID>> getUserChatRooms(String userName);
}
