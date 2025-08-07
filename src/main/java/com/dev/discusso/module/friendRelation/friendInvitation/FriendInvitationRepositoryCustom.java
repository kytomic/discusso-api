package com.dev.discusso.module.friendRelation.friendInvitation;

import com.dev.discusso.dto.requestSpecific.GetPendingFriendInvitationListResponseDto;
import com.dev.discusso.schema.FriendInvitation;
import com.dev.discusso.schema.FriendRelation;

import java.util.Optional;
import java.util.UUID;

public interface FriendInvitationRepositoryCustom {
  public GetPendingFriendInvitationListResponseDto findByInvitedUserIdAndIsPendingWithPagination(UUID invitedUserId, boolean isPending, int page, int size);

  public Optional<FriendInvitation> findByRequestUserIdAndInvitedUsername(UUID requestUserId, String invitedUserDisplayName);
}
