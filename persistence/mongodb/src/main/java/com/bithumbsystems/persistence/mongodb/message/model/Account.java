package com.bithumbsystems.persistence.mongodb.message.model;

import com.bithumbsystems.persistence.mongodb.message.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Account {
  private String accountId;
  private String email;
  private Role role;
}
