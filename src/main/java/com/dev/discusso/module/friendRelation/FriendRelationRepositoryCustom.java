package com.dev.discusso.module.friendRelation;

import com.dev.discusso.dto.requestSpecific.GetFriendRelationWithNoMessagesResponseDto;
import com.dev.discusso.schema.FriendRelation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendRelationRepositoryCustom {
  Optional<FriendRelation> findByUserId1AndUserId2(UUID userId1, UUID userId2);

  GetFriendRelationWithNoMessagesResponseDto findWithNoChatMessages(int page, int size, UUID userId, String search);
}
