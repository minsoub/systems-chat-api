package com.bithumbsystems.chat.api.v1.message.model.request;

import java.util.UUID;
import javax.management.relation.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MessageRequest {
    UUID accountId;
    Role role;
    String content;
    UUID chatRoomId;
}
