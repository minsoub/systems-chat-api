package com.bithumbsystems.chat.api.v1.message.model.response;

import com.bithumbsystems.persistence.mongodb.message.model.enums.Role;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MessageResponse {
    String accountId;
    Role role;
    String content;
    String chatRoom;
    String siteId;
    LocalDateTime createDate;
}
