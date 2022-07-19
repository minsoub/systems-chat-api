package com.bithumbsystems.chat.api.v1.message.model.response;

import com.bithumbsystems.persistence.mongodb.message.model.enums.Role;
import com.mongodb.client.model.changestream.OperationType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class MessageResponse {
    String id;
    String accountId;
    String email;
    Role role;
    String content;
    String chatRoom;
    String siteId;
    OperationType operationType;
    LocalDateTime createDate;
}
