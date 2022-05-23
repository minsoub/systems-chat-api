package com.bithumbsystems.persistence.mongodb.message.model.entity;

import com.bithumbsystems.persistence.mongodb.message.model.enums.Role;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.bson.BsonTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("user_resume_tokens")
@Getter
public class UserResumeToken {
    @Id
    private UUID uuid;

    @Indexed(unique = true)
    private final UUID accountId;

    private final Role role;

    @Setter
    private BsonTimestamp tokenTimestamp;

    public UserResumeToken(UUID accountId, Role role) {
        this.accountId = accountId;
        this.role = role;
        this.uuid = UUID.randomUUID();
        this.tokenTimestamp = new BsonTimestamp((int) Instant.now().getEpochSecond(), 0);
    }
}
