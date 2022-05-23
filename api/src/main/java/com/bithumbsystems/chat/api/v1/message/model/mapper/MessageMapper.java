package com.bithumbsystems.chat.api.v1.message.model.mapper;

import com.bithumbsystems.chat.api.v1.message.model.request.MessageRequest;
import com.bithumbsystems.persistence.mongodb.message.model.entity.ChatMessage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {

  ChatMessage messageRequestToChatMessage(MessageRequest messageRequest);
}
