package com.bithumbsystems.persistence.mongodb.message.model.entity;

import com.bithumbsystems.persistence.mongodb.message.model.enums.Role;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("chat_channel")
@Getter
@Setter
@CompoundIndex(def = "{'account_id' : 1, 'role': 1}", unique = true)
@AllArgsConstructor
@NoArgsConstructor
public class ChatChannel {

    @Id
    private String id;

    private Role role;
    private String email;

    private String accountId;

    private Set<String> chatRooms; // 프로젝트

    private String siteId;

    public ChatChannel(String accountId, Role role, final Set<String> chatRooms, String siteId) {
        this.id = String.valueOf(UUID.randomUUID());
        this.accountId = accountId;
        this.role = role;
        this.chatRooms = chatRooms;
        this.siteId = siteId;
    }

    public void addChatRoom(final String chatRoom) {
        chatRooms.add(chatRoom);
    }
}
