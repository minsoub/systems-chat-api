package com.bithumbsystems.chat.api.v1.message.service.validator;

import com.bithumbsystems.chat.api.v1.message.exception.ChatException;
import com.bithumbsystems.chat.api.v1.message.model.enums.ErrorCode;
import com.bithumbsystems.persistence.mongodb.message.model.Account;
import com.bithumbsystems.persistence.mongodb.message.model.entity.ChatChannel;
import com.bithumbsystems.persistence.mongodb.message.service.ChatChannelDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ChatValidator {

  private final ChatChannelDomainService chatChannelDomainService;

  public Mono<ChatChannel> checkValidChatRoom(final Account account, final String chatRoom) {
    return chatChannelDomainService.findByAccountIdAndRoleAndChatRoomsContains(
            account.getAccountId(), account.getRole(), chatRoom)
        .switchIfEmpty(Mono.error(new ChatException(ErrorCode.INVALID_CHAT_ROOM)));
  }
}