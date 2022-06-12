package com.bithumbsystems.chat.api.v1.message.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ChannelRequest {
    String chatRoom;
    String siteId;
}
