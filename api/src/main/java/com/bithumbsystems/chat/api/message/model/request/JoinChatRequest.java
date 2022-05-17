package com.bithumbsystems.chat.api.message.model.request;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JoinChatRequest {
    UUID chatId;
}
