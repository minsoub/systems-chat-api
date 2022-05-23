package com.bithumbsystems.persistence.mongodb.message.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.management.relation.Role;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("chat_message")
@Getter
@ToString
public class ChatMessage {
    @Id
    private UUID id;
    private final UUID accountId;
    private final Role role;
    private final String content;
    private final UUID projectId;

    @CreatedDate
    private LocalDateTime createDate;

    public ChatMessage(final UUID accountId, Role role, final String content, final UUID projectId) {
        this.id = UUID.randomUUID();
        this.role = role;
        this.accountId = accountId;
        this.content = content;
        this.projectId = projectId;
    }
}
