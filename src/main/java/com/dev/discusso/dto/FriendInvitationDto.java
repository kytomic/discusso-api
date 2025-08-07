package com.dev.discusso.dto;

import com.dev.discusso.schema.FriendInvitation;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class FriendInvitationDto extends BaseDto {
  private String id;
  private String requestUserId;
  private String invitedUserId;
  @JsonProperty("isAccepted")
  private boolean isAccepted;
  @JsonProperty("isPending")
  private boolean isPending;
  private LocalDateTime timestamp;

  public FriendInvitationDto(FriendInvitation friendInvitation) {
    super(friendInvitation.getCreatedAt(), friendInvitation.getCreatedBy(), friendInvitation.getUpdatedAt(), friendInvitation.getUpdatedBy(), friendInvitation.isDeleted());
    this.id = friendInvitation.getId().toString();
    this.requestUserId = friendInvitation.getRequestUserId().toString();
    this.invitedUserId = friendInvitation.getInvitedUserId().toString();
    this.isAccepted = friendInvitation.isAccepted();
    this.isPending = friendInvitation.isPending();
    this.timestamp = friendInvitation.getTimestamp();
  }
}
