package com.bithumbsystems.chat.api.v1.message.service;

import com.bithumbsystems.chat.api.core.config.property.AwsProperties;
import com.bithumbsystems.chat.api.v1.message.model.request.MessageRequest;
import com.bithumbsystems.chat.api.v1.message.model.response.ChatMessageResponse;
import com.bithumbsystems.chat.api.v1.message.service.validator.ChatValidator;
import com.bithumbsystems.chat.api.v1.message.util.AES256Util;
import com.bithumbsystems.persistence.mongodb.message.model.Account;
import com.bithumbsystems.persistence.mongodb.message.model.entity.ChatChannel;
import com.bithumbsystems.persistence.mongodb.message.model.entity.ChatMessage;
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
  private final ChatValidator chatValidator;

  public Flux<ChatMessageResponse> connectChatRoom(final Account account, final String chatRoom,
      final String siteId) {
    return chatValidator.checkValidChatRoom(account, chatRoom)
        .as(chatChannel -> chatMessageDomainService.findMessages(chatRoom, siteId))
        .defaultIfEmpty(new ChatMessage())
        .flatMap(chatMessage -> Mono.just(ChatMessageResponse.builder()
            .id(chatMessage.getId())
            .accountId(chatMessage.getAccountId())
            .email(AES256Util.decryptAES(awsProperties.getKmsKey(), chatMessage.getEmail()))
            .name(
                AES256Util.decryptAES(awsProperties.getKmsKey(), chatMessage.getName()))  // new add
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
        .flatMapIterable(ChatChannel::getChatRooms)
        .collect(Collectors.toSet())
        .defaultIfEmpty(Set.of());
  }

  public Mono<ChatMessageResponse> saveMessage(final MessageRequest chatMessageRequest,
      final Account account) {
    return chatValidator.checkValidChatRoom(account, chatMessageRequest.getChatRoom())
        .flatMap(chatChannel -> chatMessageDomainService.save(
                ChatMessage.builder()
                    .accountId(chatChannel.getAccountId())
                    .email(AES256Util.encryptAES(awsProperties.getKmsKey(), account.getEmail(), awsProperties.getSaltKey(), awsProperties.getIvKey()))
                    .name(AES256Util.encryptAES(
                        awsProperties.getKmsKey(),
                        AES256Util.decryptAES(awsProperties.getCryptoKey(),
                            chatMessageRequest.getName()),
                        awsProperties.getSaltKey(),
                        awsProperties.getIvKey()
                    ))  // new add
                    .role(account.getRole())
                    .content(AES256Util.encryptAES(awsProperties.getKmsKey(),
                        chatMessageRequest.getContent(), awsProperties.getSaltKey(),
                        awsProperties.getIvKey()))
                    .chatRoom(chatMessageRequest.getChatRoom())
                    .isDelete(false)
                    .siteId(chatMessageRequest.getSiteId())
                    .build()
            ))
            .flatMap(chatMessage -> Mono.just(ChatMessageResponse.builder()
                .id(chatMessage.getId())
                .accountId(chatMessage.getAccountId())
                .email(AES256Util.decryptAES(awsProperties.getKmsKey(), chatMessage.getEmail()))
                .name(AES256Util.decryptAES(awsProperties.getKmsKey(),
                    chatMessage.getName())) // new add
                .role(chatMessage.getRole())
                .content(AES256Util.decryptAES(awsProperties.getKmsKey(), chatMessage.getContent()))
                .chatRoom(chatMessage.getChatRoom())
                .createDate(chatMessage.getCreateDate())
                .build()));
  }

}
