package com.dev.discusso.dto.requestSpecific;

import com.dev.discusso.dto.PaginatedRequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetPendingFriendInvitationListRequestDto extends PaginatedRequestDto {
  private String invitedUserId;

  public GetPendingFriendInvitationListRequestDto(String invitedUserId, int page, int pageSize) {
    this.invitedUserId = invitedUserId;
    this.setPage(page);
    this.setPageSize(pageSize);
  }
}
