package com.bithumbsystems.chat.api.v1.message.service;

import com.bithumbsystems.chat.api.core.config.property.AwsProperties;
import com.bithumbsystems.chat.api.v1.message.model.request.ChannelRequest;
import com.bithumbsystems.chat.api.v1.message.model.response.MessageResponse;
import com.bithumbsystems.chat.api.v1.message.service.validator.ChatValidator;
import com.bithumbsystems.chat.api.v1.message.util.AES256Util;
import com.bithumbsystems.persistence.mongodb.message.model.Account;
import com.bithumbsystems.persistence.mongodb.message.service.ChatMessageDomainService;
import com.bithumbsystems.persistence.mongodb.message.service.ChatResumeTokenDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonTimestamp;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatWatcherService {

  private final ChatResumeTokenDomainService resumeTokenService;
  private final ChatMessageDomainService chatMessageDomainService;
  private final ChatValidator chatValidator;
  private final AwsProperties awsProperties;

  public Flux<MessageResponse> channelMessages(final ChannelRequest channelRequest,
      Account account) {
    return chatValidator.checkValidChatRoom(account, channelRequest.getChatRoom()).flatMap(chatChannel ->
            resumeTokenService.getResumeTimestamp(chatChannel.getSiteId(),
                channelRequest.getChatRoom()))
        .flatMapMany(bsonTimestamp -> changeStream(channelRequest, bsonTimestamp))
        .doOnCancel(() -> resumeTokenService.saveAndGenerateNewTokenFor(channelRequest.getSiteId(),
            channelRequest.getChatRoom()));
  }

  private Flux<MessageResponse> changeStream(final ChannelRequest channelRequest,
      final BsonTimestamp bsonTimestamp) {
    return chatMessageDomainService.changeStream(channelRequest.getChatRoom(),
            channelRequest.getSiteId(), bsonTimestamp)
        .map(chatMessage -> new MessageResponse(
            chatMessage.getId(),
            chatMessage.getAccountId(),
            AES256Util.decryptAES(awsProperties.getKmsKey(), chatMessage.getEmail()),
            AES256Util.decryptAES(awsProperties.getKmsKey(), chatMessage.getName()), // new add
            chatMessage.getRole(),
            AES256Util.decryptAES(awsProperties.getKmsKey(), chatMessage.getContent()),
            chatMessage.getChatRoom(),
            chatMessage.getSiteId(),
            chatMessage.getOperationType(),
            chatMessage.getCreateDate())
        );
  }
}
