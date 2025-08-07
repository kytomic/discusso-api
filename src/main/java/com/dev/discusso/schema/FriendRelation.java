package com.dev.discusso.schema;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "friend_relation", schema = "public")
public class FriendRelation extends BaseModel {
  @Id
  @GeneratedValue
  @Column(columnDefinition = "uuid", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "user_id_1")
  private UUID userId1;

  @Column(name = "user_id_2")
  private UUID userId2;

  @Column(name = "invitation_id")
  private UUID invitationId;

  public FriendRelation(String userId1, String userId2, String invitationId) {
    super("admin", "admin");
    this.userId1 = UUID.fromString(userId1);
    this.userId2 = UUID.fromString(userId2);
    this.invitationId = UUID.fromString(invitationId);
  }
}
