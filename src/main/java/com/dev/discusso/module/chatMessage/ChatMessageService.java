package com.dev.discusso.module.chatMessage;

import com.dev.discusso.dto.ChatMessageDto;
import com.dev.discusso.dto.requestSpecific.GetChatMessageListResponseDto;
import com.dev.discusso.dto.requestSpecific.GetLatestChatMessageListResponseDto;

public interface ChatMessageService {
  public ChatMessageDto create(ChatMessageDto chatMessageDto);

  public GetLatestChatMessageListResponseDto getLatestChatMessageList(String userId, int page, int size, String searchUsername);

  public GetChatMessageListResponseDto getChatMessageList(String userId, String friendId, int page, int size);
}
