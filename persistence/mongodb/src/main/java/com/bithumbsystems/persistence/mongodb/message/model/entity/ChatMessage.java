package com.bithumbsystems.persistence.mongodb.message.model.entity;

import com.bithumbsystems.persistence.mongodb.message.model.enums.Role;
import com.mongodb.client.model.changestream.OperationType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("chat_message")
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndex(def = "{'chat_room' : 1, 'site_id': 1, 'is_delete': 1}")
public class ChatMessage {

    @Id
    private String id;
    private String accountId;
    private String email;
    private String name;
    private Role role;
    private String content;
    private String chatRoom;
    private String siteId;
    private Boolean isDelete;
    private LocalDateTime deleteDate;
    private LocalDateTime createDate;

    @Transient
    private OperationType operationType;
}
