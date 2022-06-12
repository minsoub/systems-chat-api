package com.bithumbsystems.chat.api.v1.message.model.mapper;

import com.bithumbsystems.chat.api.v1.message.model.request.MessageRequest;
import com.bithumbsystems.chat.api.v1.message.model.response.ChatMessageResponse;
import com.bithumbsystems.persistence.mongodb.message.model.entity.ChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MessageMapper {

  MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

  ChatMessage messageRequestToChatMessage(MessageRequest messageRequest);

  ChatMessageResponse chatMessageToChatMessageResponse(ChatMessage chatMessage);

}
