package com.dev.discusso.dto.requestSpecific;

import com.dev.discusso.dto.FriendInvitationDto;
import com.dev.discusso.dto.PaginationResponseDto;
import com.dev.discusso.schema.FriendInvitation;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class GetPendingFriendInvitationListResponseDto extends PaginationResponseDto {
  private GetFriendInvitationResponseItem[] pendingInvitations;

  @Data
  public static class GetFriendInvitationResponseItem extends FriendInvitationDto {
    private String invitedUserDisplayName;

    public GetFriendInvitationResponseItem(FriendInvitation friendInvitation, String invitedUserDisplayName) {
      super(friendInvitation);
      this.invitedUserDisplayName = invitedUserDisplayName;
    }
  }

  public GetPendingFriendInvitationListResponseDto(
      int page,
      int pageSize,
      int totalCount,
      GetFriendInvitationResponseItem[] pendingInvitations
  ) {
    super(page, pageSize, totalCount);
    this.pendingInvitations = pendingInvitations;
  }
}
