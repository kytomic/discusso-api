package com.dev.discusso.dto.requestSpecific;

import com.dev.discusso.dto.FriendInvitationDto;
import com.dev.discusso.schema.FriendInvitation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CreateFriendInvitationResponseDto extends FriendInvitationDto {
  public CreateFriendInvitationResponseDto(FriendInvitation friendInvitation) {
    super(friendInvitation);
  }
}
