package com.bithumbsystems.chat.api.v1.message.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JoinChatRequest {
  private String chatRoom;
  private String siteId;
}
