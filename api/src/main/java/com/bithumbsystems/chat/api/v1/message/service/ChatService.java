package com.bithumbsystems.chat.api.v1.message.service;

import com.bithumbsystems.chat.api.core.config.property.AwsProperties;
import com.bithumbsystems.chat.api.v1.message.model.request.MessageRequest;
import com.bithumbsystems.chat.api.v1.message.model.response.ChatMessageResponse;
import com.bithumbsystems.chat.api.v1.message.util.AES256Util;
import com.bithumbsystems.persistence.mongodb.message.model.Account;
import com.bithumbsystems.persistence.mongodb.message.model.entity.ChatChannel;
import com.bithumbsystems.persistence.mongodb.message.model.entity.ChatMessage;
import com.bithumbsystems.persistence.mongodb.message.model.enums.Role;
import com.bithumbsystems.persistence.mongodb.message.service.ChatChannelDomainService;
import com.bithumbsystems.persistence.mongodb.message.service.ChatMessageDomainService;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public
class ChatService {

  private final AwsProperties awsProperties;
  private final ChatChannelDomainService chatChannelDomainService;
  private final ChatMessageDomainService chatMessageDomainService;

  public Flux<ChatMessageResponse> connectChatRoom(final Account account, final String chatRoom,
      final String siteId) {
    return chatChannelDomainService.findByAccountIdAndRoleAndChatRoomsContains(
            account.getAccountId(), account.getRole(), chatRoom)
        .as(chatChannel -> chatMessageDomainService.findMessages(chatRoom, siteId))
        .defaultIfEmpty(new ChatMessage())
        .flatMap(chatMessage -> Mono.just(ChatMessageResponse.builder()
            .id(chatMessage.getId())
            .accountId(chatMessage.getAccountId())
            .email(AES256Util.decryptAES(awsProperties.getKmsKey(), chatMessage.getEmail()))
            .name(chatMessage.getName())  // new add
            .role(chatMessage.getRole())
            .content(AES256Util.decryptAES(awsProperties.getKmsKey(), chatMessage.getContent()))
            .chatRoom(chatMessage.getChatRoom())
            .createDate(chatMessage.getCreateDate())
            .build()));
  }

  public Mono<ChatChannel> createChatRoom(final Account account, final String chatRoom,
      final String siteId) {
    ChatChannel chatChannel = new ChatChannel(account.getAccountId(), account.getRole(),
        new HashSet<>(), siteId);
    chatChannel.addChatRoom(chatRoom);
    return chatChannelDomainService.save(chatChannel);
  }

  public Mono<Set<String>> getChatRooms(final Account account, final String siteId) {
    return chatChannelDomainService.findByAccountIdAndRoleAndSiteId(account.getAccountId(),
            account.getRole(), siteId)
        .doOnNext(chatChannel -> log.info("found user {} {}", chatChannel.getAccountId(),
            chatChannel.getRole()))
        .flatMapIterable(ChatChannel::getChatRooms)
        .collect(Collectors.toSet())
        .doOnNext(uuids -> log.info("set size {}", uuids.size()))
        .defaultIfEmpty(Set.of())
        .doOnNext(uuids -> log.info("set size {}", uuids.size()));
  }

  public Mono<ChatMessageResponse> saveMessage(final MessageRequest chatMessageRequest,
      final Account account) {
    return chatMessageDomainService.save(
            ChatMessage.builder()
                .accountId(account.getAccountId())
                .email(account.getRole().equals(Role.USER) ? account.getEmail() : AES256Util.encryptAES(awsProperties.getKmsKey(),
                    account.getEmail()))
                .name(chatMessageRequest.getName())  // new add
                .role(account.getRole())
                .content(AES256Util.encryptAES(awsProperties.getKmsKey(),
                        chatMessageRequest.getContent()))
                .chatRoom(chatMessageRequest.getChatRoom())
                .isDelete(false)
                .siteId(chatMessageRequest.getSiteId())
                .build()
        ).flatMap(chatMessage -> Mono.just(ChatMessageResponse.builder()
            .id(chatMessage.getId())
            .accountId(chatMessage.getAccountId())
            .email(AES256Util.decryptAES(awsProperties.getKmsKey(), chatMessage.getEmail()))
            .name(chatMessage.getName()) // new add
            .role(chatMessage.getRole())
            .content(AES256Util.decryptAES(awsProperties.getKmsKey(), chatMessage.getContent()))
            .chatRoom(chatMessage.getChatRoom())
            .createDate(chatMessage.getCreateDate())
            .build()));
  }
}
