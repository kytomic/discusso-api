package com.dev.discusso.schema;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "friend_invitation", schema = "public")
public class FriendInvitation extends BaseModel {
  @Id
  @GeneratedValue
  @Column(columnDefinition = "uuid", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "request_user_id")
  private UUID requestUserId;

  @Column(name = "invited_user_id")
  private UUID invitedUserId;

  @Column(name = "is_accepted")
  private boolean isAccepted;

  @Column(name = "is_pending")
  private boolean isPending;

  @Column(name = "timestamp")
  private LocalDateTime timestamp;

  public FriendInvitation(String requestUserId, String invitedUserId, boolean isAccepted, boolean isPending, LocalDateTime timestamp) {
    super("system", "system");
    this.requestUserId = UUID.fromString(requestUserId);
    this.invitedUserId = UUID.fromString(invitedUserId);
    this.isAccepted = isAccepted;
    this.isPending = isPending;
    this.timestamp = timestamp;
  }
}
