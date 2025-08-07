package com.dev.discusso.dto;

import com.dev.discusso.schema.ChatMessage;
import com.dev.discusso.schema.FriendRelation;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class FriendRelationDto extends BaseDto {
  private String id;
  private String userId1;
  private String userId2;
  private String invitationId;

  public FriendRelationDto(FriendRelation friendRelation) {
    this.id = friendRelation.getId().toString();
    this.userId1 = friendRelation.getUserId1().toString();
    this.userId2 = friendRelation.getUserId2().toString();
    this.invitationId = friendRelation.getInvitationId().toString();
    this.setCreatedAt(friendRelation.getCreatedAt());
    this.setCreatedBy(friendRelation.getCreatedBy());
    this.setUpdatedAt(friendRelation.getUpdatedAt());
    this.setUpdatedBy(friendRelation.getUpdatedBy());
  }
}
