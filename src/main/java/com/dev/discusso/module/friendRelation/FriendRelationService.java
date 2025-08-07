package com.dev.discusso.module.friendRelation;

import com.dev.discusso.dto.FriendInvitationDto;
import com.dev.discusso.dto.requestSpecific.*;
import com.dev.discusso.schema.FriendInvitation;
import com.dev.discusso.schema.FriendRelation;

import java.util.List;
import java.util.UUID;

public interface FriendRelationService {
  public GetPendingFriendInvitationListResponseDto getFriendInvitationList(
    GetPendingFriendInvitationListRequestDto dto
  );

  public CreateFriendInvitationResponseDto createFriendInvitation(CreateFriendInvitationRequestDto dto);

  public FriendInvitationDto updateInvitation(String invitationId, boolean isAccepted);

  public GetFriendRelationWithNoMessagesResponseDto findWithNoChatMessages(int page, int size, String userId, String search);
}
