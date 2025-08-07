package com.dev.discusso.dto.requestSpecific;

import com.dev.discusso.dto.FriendRelationDto;
import com.dev.discusso.dto.PaginationResponseDto;
import com.dev.discusso.schema.FriendRelation;
import lombok.Data;

@Data
public class GetFriendRelationWithNoMessagesResponseDto extends PaginationResponseDto {
  private GetFriendRelationWithNoMessagesItemDto[] data;

  @Data
  public static class GetFriendRelationWithNoMessagesItemDto extends FriendRelationDto {
    private String friendName;

    public GetFriendRelationWithNoMessagesItemDto(FriendRelation friendRelation, String friendName) {
      super(friendRelation);
      this.friendName = friendName;
    }
  }

  public GetFriendRelationWithNoMessagesResponseDto(
    int page,
    int pageSize,
    int totalCount,
    GetFriendRelationWithNoMessagesItemDto[] data
  ) {
    super(page, pageSize, totalCount);
    this.data = data;
  }
}
