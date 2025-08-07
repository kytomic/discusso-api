package com.dev.discusso.dto.requestSpecific;

import com.dev.discusso.dto.ChatMessageDto;
import com.dev.discusso.dto.PaginationResponseDto;
import com.dev.discusso.schema.ChatMessage;
import lombok.Data;

@Data
public class GetLatestChatMessageListResponseDto extends PaginationResponseDto {
  private GetLatestChatMessageItemDto[] data;

  @Data
  public static class GetLatestChatMessageItemDto extends ChatMessageDto {
    private String senderName;

    public GetLatestChatMessageItemDto(ChatMessage chatMessage, String senderName) {
      super(chatMessage);
      this.senderName = senderName;
    }
  }

  public GetLatestChatMessageListResponseDto(
    int page,
    int pageSize,
    int totalCount,
    GetLatestChatMessageItemDto[] data
  ) {
    super(page, pageSize, totalCount);
    this.data = data;
  }
}
