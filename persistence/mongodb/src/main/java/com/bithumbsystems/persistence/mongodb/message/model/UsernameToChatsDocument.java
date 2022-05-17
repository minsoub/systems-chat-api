package com.bithumbsystems.persistence.mongodb.message.model;

import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("userChats")
@Getter
public class UsernameToChatsDocument {
    @Id
    private UUID id;
    @Indexed(unique = true)
    private String userName;
    private Set<UUID> chats;

    public UsernameToChatsDocument(final String userName, final Set<UUID> chats) {
        this.userName = userName;
        this.chats = chats;
        this.id = UUID.randomUUID();
    }

    public void addChat(final UUID chat) {
        chats.add(chat);
    }
}
