package com.dev.discusso.module.chatMessage;

import com.dev.discusso.dto.requestSpecific.GetChatMessageListResponseDto;
import com.dev.discusso.dto.requestSpecific.GetLatestChatMessageListResponseDto;

import java.util.UUID;

public interface ChatMessageRepositoryCustom {
  GetLatestChatMessageListResponseDto findLatestChatMessageList(UUID userId, int page, int size, String searchUsername);

  GetChatMessageListResponseDto findChatMessageList(UUID userId, UUID friendId, int page, int size);
}
