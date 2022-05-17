package com.bithumbsystems.persistence.mongodb.message.model;

import java.util.UUID;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("messages")
@Getter
@ToString
public class MessageDocument {
    @Id
    private UUID id;
    private String usernameFrom;
    private String content;
    private UUID chatRoomId;

    public MessageDocument(final String usernameFrom, final String content, final UUID chatRoomId) {
        this.id = UUID.randomUUID();
        this.usernameFrom = usernameFrom;
        this.content = content;
        this.chatRoomId = chatRoomId;
    }

    public boolean isNotFromUser(final String usernameFrom) {
        return !this.usernameFrom.equals(usernameFrom);
    }

}
