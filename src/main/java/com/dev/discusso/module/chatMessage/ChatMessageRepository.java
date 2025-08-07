package com.dev.discusso.module.chatMessage;

import com.dev.discusso.schema.ChatMessage;
import com.dev.discusso.schema.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
  public ChatMessage save(ChatMessage chatMessage);
}
