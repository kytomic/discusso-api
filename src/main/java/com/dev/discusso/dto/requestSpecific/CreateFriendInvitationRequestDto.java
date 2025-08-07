package com.dev.discusso.dto.requestSpecific;

import com.dev.discusso.schema.FriendInvitation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFriendInvitationRequestDto {
  private String requestUserId;
  private String invitedUserDisplayName;
}
