package com.bithumbsystems.persistence.mongodb.message.model.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.bson.BsonTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("chat_resume_token")
@Getter
@CompoundIndexes({
    @CompoundIndex(name = "site_chat_room", def = "{'chatRoom' : 1, 'siteId': 1}", unique = true)
})
public class ChatResumeToken {
    @Id
    private String id;
    private final String siteId;
    private final String chatRoom;
    @Setter
    private BsonTimestamp tokenTimestamp;

    public ChatResumeToken(String siteId, String chatRoom) {
        this.siteId = siteId;
        this.chatRoom = chatRoom;
        this.id = String.valueOf(UUID.randomUUID());
        this.tokenTimestamp = new BsonTimestamp((int) Instant.now().getEpochSecond(), 0);
    }
}
