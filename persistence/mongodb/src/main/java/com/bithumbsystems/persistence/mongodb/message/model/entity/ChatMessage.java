package com.bithumbsystems.persistence.mongodb.message.model.entity;

import com.bithumbsystems.persistence.mongodb.message.model.enums.Role;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("chat_message")
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndex(name = "site_chat_room", def = "{'chat_room' : 1, 'site_id': 1}")
public class ChatMessage {
    @Id
    private String id;
    private String accountId;
    private String email;
    private Role role;
    private String content;
    private String chatRoom;
    private String siteId;
    private LocalDateTime createDate;
}
