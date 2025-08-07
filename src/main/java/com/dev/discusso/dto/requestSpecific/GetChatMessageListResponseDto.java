package com.dev.discusso.dto.requestSpecific;

import com.dev.discusso.dto.ChatMessageDto;
import com.dev.discusso.dto.PaginationResponseDto;
import com.dev.discusso.schema.ChatMessage;
import lombok.Data;

@Data
public class GetChatMessageListResponseDto extends PaginationResponseDto {
  private GetChatMessageItemDto[] data;

  @Data
  public static class GetChatMessageItemDto extends ChatMessageDto {
    private String friendName;

    public GetChatMessageItemDto(ChatMessage chatMessage, String friendName) {
      super(chatMessage);
      this.friendName = friendName;
    }
  }

  public GetChatMessageListResponseDto(
    int page,
    int pageSize,
    int totalCount,
    GetChatMessageItemDto[] data
  ) {
    super(page, pageSize, totalCount);
    this.data = data;
  }
}
