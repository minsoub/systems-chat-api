package com.bithumbsystems.persistence.mongodb.message.model.entity;

import com.bithumbsystems.persistence.mongodb.message.model.enums.Role;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("chat_room")
@Getter
@CompoundIndexes({
    @CompoundIndex(name = "role_accountId", def = "{'accountId' : 1, 'role': 1}")
})
public class ChatRoom {

    @Id
    private final UUID id;

    private final Role role;

    private final UUID accountId;

    private final Set<UUID> projects;

    public ChatRoom(UUID accountId, Role role, final Set<UUID> projects) {
        this.id = UUID.randomUUID();
        this.accountId = accountId;
        this.role = role;
        this.projects = projects;
    }

    public void addChat(final UUID projectID) {
        projects.add(projectID);
    }
}
