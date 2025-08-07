package com.dev.discusso.dto.requestSpecific;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateFriendInvitationRequestDto {
  @JsonProperty("isAccepted")
  private boolean isAccepted;
}
