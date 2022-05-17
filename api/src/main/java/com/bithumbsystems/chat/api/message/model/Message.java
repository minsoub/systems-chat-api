package com.bithumbsystems.chat.api.message.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Message {
    String usernameFrom;
    String content;
    UUID chatRoomId;
}
