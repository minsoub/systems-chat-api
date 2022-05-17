package com.bithumbsystems.chat.api.message.model.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public
class ChatCreatedResponse {
    UUID chatId;
}
