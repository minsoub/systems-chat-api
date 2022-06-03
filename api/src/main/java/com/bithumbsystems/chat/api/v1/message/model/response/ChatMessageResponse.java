package com.bithumbsystems.chat.api.v1.message.model.response;

import com.bithumbsystems.persistence.mongodb.message.model.enums.Role;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatMessageResponse {
    String accountId;
    Role role;
    String content;
    String chatRoom;
    LocalDateTime createDate;
}
