package com.dev.discusso.dto;

import com.dev.discusso.schema.ChatMessage;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ChatMessageDto extends BaseDto {
  private String id;

  private String senderId;

  private String receiverId;

  private String content;

  @JsonProperty("isRead")
  private boolean isRead;

  private LocalDateTime timestamp;

  public ChatMessageDto(ChatMessage chatMessage) {
    super(chatMessage.getCreatedBy(), chatMessage.getUpdatedBy());
    this.id = chatMessage.getId().toString();
    this.senderId = chatMessage.getSenderId().toString();
    this.receiverId = chatMessage.getReceiverId().toString();
    this.content = chatMessage.getContent();
    this.isRead = chatMessage.isRead();
    this.timestamp = chatMessage.getTimestamp();
  }
}
