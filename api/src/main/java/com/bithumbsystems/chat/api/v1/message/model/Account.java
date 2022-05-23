package com.bithumbsystems.chat.api.v1.message.model;

import com.bithumbsystems.persistence.mongodb.message.model.enums.Role;
import java.util.UUID;
import lombok.Data;

@Data
public class Account {

  private UUID accountId;
  private Role role;
  private UUID projectId;
}
