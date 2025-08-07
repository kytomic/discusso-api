package com.dev.discusso.schema;

import com.dev.discusso.dto.ChatMessageDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chat_message")
public class ChatMessage extends BaseModel {
  @Id
  @GeneratedValue
  @Column(columnDefinition = "uuid", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "sender_id", nullable = false)
  private UUID senderId;

  @Column(name = "receiver_id", nullable = false)
  private UUID receiverId;

  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "is_read", nullable = false)
  @JsonProperty("isRead")
  public boolean isRead;

  @Column(name = "timestamp")
  private LocalDateTime timestamp;

  public ChatMessage(String senderId, String receiverId, String content, LocalDateTime timestamp, String createdBy, String updatedBy) {
    super(createdBy, updatedBy);
    this.senderId = UUID.fromString(senderId);
    this.receiverId = UUID.fromString(receiverId);
    this.content = content;
    this.isRead = false;
    this.timestamp = timestamp == null ? LocalDateTime.now() : timestamp;
  }

  public ChatMessage(ChatMessageDto chatMessageDto) {
    super(chatMessageDto.getCreatedBy(), chatMessageDto.getUpdatedBy());
    this.senderId = UUID.fromString(chatMessageDto.getSenderId());
    this.receiverId = UUID.fromString(chatMessageDto.getReceiverId());
    this.content = chatMessageDto.getContent();
    this.isRead = chatMessageDto.isRead();
    this.timestamp = chatMessageDto.getTimestamp();
  }

  public boolean isRead() {
    return this.isRead;
  }
}
