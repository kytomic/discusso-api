package com.dev.discusso.module.chatMessage;

import com.dev.discusso.dto.ChatMessageDto;
import com.dev.discusso.dto.requestSpecific.GetChatMessageListResponseDto;
import com.dev.discusso.dto.requestSpecific.GetLatestChatMessageListResponseDto;
import com.dev.discusso.schema.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {
  private final ChatMessageRepository chatMessageRepository;
  private final ChatMessageRepositoryCustom chatMessageRepositoryCustom;

  public ChatMessageServiceImpl(ChatMessageRepository chatMessageRepository, ChatMessageRepositoryCustom chatMessageRepositoryCustom) {
    this.chatMessageRepository = chatMessageRepository;
    this.chatMessageRepositoryCustom = chatMessageRepositoryCustom;
  }

  public ChatMessageDto create(ChatMessageDto chatMessageDto) {
    ChatMessage chatMessage = new ChatMessage(chatMessageDto);
    ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
    return new ChatMessageDto(savedMessage);
  }

  public GetLatestChatMessageListResponseDto getLatestChatMessageList(String userId, int page, int size, String searchUsername) {
    return chatMessageRepositoryCustom.findLatestChatMessageList(UUID.fromString(userId), page, size, searchUsername);
  }

  public GetChatMessageListResponseDto getChatMessageList(String userId, String friendId, int page, int size) {
    return chatMessageRepositoryCustom.findChatMessageList(UUID.fromString(userId), UUID.fromString(friendId), page, size);
  }
}
