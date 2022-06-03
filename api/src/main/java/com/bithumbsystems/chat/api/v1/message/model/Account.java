package com.bithumbsystems.chat.api.v1.message.model;

import com.bithumbsystems.persistence.mongodb.message.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Account {
  private String accountId;
  private Role role;
}
