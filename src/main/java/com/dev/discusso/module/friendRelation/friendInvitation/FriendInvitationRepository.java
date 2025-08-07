package com.dev.discusso.module.friendRelation.friendInvitation;

import com.dev.discusso.schema.FriendInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FriendInvitationRepository extends JpaRepository<FriendInvitation, UUID> {
  public FriendInvitation save(FriendInvitation friendInvitation);

  public FriendInvitation findOneByRequestUserIdAndInvitedUserId(UUID requestUserId, UUID invitedUserId);
}
