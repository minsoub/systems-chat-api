package com.bithumbsystems.chat.api.v1.message.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MessageRequest {
    String content;
    String chatRoom;
    String siteId;
    String name;
}
