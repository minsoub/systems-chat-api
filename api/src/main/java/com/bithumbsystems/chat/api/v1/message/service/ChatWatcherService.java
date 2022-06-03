package com.bithumbsystems.chat.api.v1.message.service;

import com.bithumbsystems.chat.api.v1.message.model.request.MessageRequest;
import com.bithumbsystems.chat.api.v1.message.model.response.MessageResponse;
import com.bithumbsystems.persistence.mongodb.message.service.ChatMessageDomainService;
import com.bithumbsystems.persistence.mongodb.message.service.ChatResumeTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonTimestamp;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatWatcherService {

    private final ChatResumeTokenService resumeTokenService;
    private final ChatMessageDomainService chatMessageDomainService;

    public Flux<MessageResponse> sendMessages(final MessageRequest messageRequest) {
        return resumeTokenService.getResumeTimestamp(messageRequest.getSiteId(), messageRequest.getChatRoom())
            .flatMapMany(bsonTimestamp -> changeStream(messageRequest, bsonTimestamp))
            .doOnCancel(() -> resumeTokenService.saveAndGenerateNewTokenFor(messageRequest.getSiteId(), messageRequest.getChatRoom()));
    }

    private Flux<MessageResponse> changeStream(final MessageRequest messageRequest, final BsonTimestamp bsonTimestamp) {
        return chatMessageDomainService.changeStream(messageRequest.getChatRoom(), messageRequest.getSiteId(), bsonTimestamp)
            .map(chatMessage -> new MessageResponse(
                chatMessage.getAccountId(),
                chatMessage.getRole(),
                chatMessage.getContent(),
                chatMessage.getChatRoom(),
                chatMessage.getSiteId(),
                chatMessage.getCreateDate())
            );
    }
}
