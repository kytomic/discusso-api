package com.dev.discusso.module.friendRelation;

import com.dev.discusso.common.exception.CustomException;
import com.dev.discusso.dto.FriendInvitationDto;
import com.dev.discusso.dto.requestSpecific.*;
import com.dev.discusso.module.friendRelation.friendInvitation.FriendInvitationRepository;
import com.dev.discusso.module.friendRelation.friendInvitation.FriendInvitationRepositoryCustom;
import com.dev.discusso.module.user.UserRepository;
import com.dev.discusso.schema.FriendInvitation;
import com.dev.discusso.schema.FriendRelation;
import com.dev.discusso.schema.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FriendRelationServiceImpl implements FriendRelationService {
  private final FriendRelationRepository friendRelationRepository;
  private final FriendRelationRepositoryImpl friendRelationRepositoryImpl;
  private final FriendInvitationRepositoryCustom friendInvitationRepositoryCustom;
  private final FriendInvitationRepository friendInvitationRepository;
  private final UserRepository userRepository;

  public FriendRelationServiceImpl(
    FriendRelationRepository friendRelationRepository,
    FriendRelationRepositoryImpl friendRelationRepositoryImpl,
    FriendInvitationRepositoryCustom friendInvitationRepositoryCustom,
    FriendInvitationRepository friendInvitationRepository,
    UserRepository userRepository
  ) {
    this.friendRelationRepository = friendRelationRepository;
    this.friendRelationRepositoryImpl = friendRelationRepositoryImpl;
    this.friendInvitationRepositoryCustom = friendInvitationRepositoryCustom;
    this.friendInvitationRepository = friendInvitationRepository;
    this.userRepository = userRepository;
  }

  public GetPendingFriendInvitationListResponseDto getFriendInvitationList(GetPendingFriendInvitationListRequestDto dto) {
    int page = dto.getPage();
    int size = dto.getPageSize();
    UUID invitedUserId = UUID.fromString(dto.getInvitedUserId());
    boolean isPending = true;

    return this.friendInvitationRepositoryCustom.findByInvitedUserIdAndIsPendingWithPagination(
      invitedUserId, isPending, page, size
    );
  }

  public CreateFriendInvitationResponseDto createFriendInvitation(CreateFriendInvitationRequestDto dto) throws CustomException {
    UUID requestUserId = UUID.fromString(dto.getRequestUserId());
    String invitedUserDisplayName = dto.getInvitedUserDisplayName();

    Optional<FriendInvitation> existingFriendInvitation =
      this.friendInvitationRepositoryCustom.findByRequestUserIdAndInvitedUsername(
        requestUserId, invitedUserDisplayName
      );

    if (existingFriendInvitation.isPresent()) {
      throw new CustomException("0014_EXISTING_FRIEND_INVITATION");
    }

    Optional<User> user = this.userRepository.findByDisplayName(invitedUserDisplayName);
    if (user.isEmpty()) {
      throw new CustomException("0010_USER_NOT_FOUND");
    }

    // Create and save the new friend invitation
    FriendInvitation friendInvitation = new FriendInvitation(
      requestUserId.toString(),
      user.get().getId().toString(),
      false,
      true,
      java.time.LocalDateTime.now()
    );
    FriendInvitation result = this.friendInvitationRepository.save(friendInvitation);
    return new CreateFriendInvitationResponseDto(friendInvitation);
  }

  public FriendInvitationDto updateInvitation(String invitationId, boolean isAccepted) throws CustomException {
    Optional<FriendInvitation> existingFriendInvitation = this.friendInvitationRepository.findById(UUID.fromString(invitationId));
    if (existingFriendInvitation.isEmpty()) {
      throw new CustomException("0015_FRIEND_INVITATION_NOT_FOUND");
    }

    existingFriendInvitation.get().setAccepted(isAccepted);
    existingFriendInvitation.get().setPending(!isAccepted);
    FriendInvitation friendInvitation = this.friendInvitationRepository.save(existingFriendInvitation.get());

    if (isAccepted) {
      FriendRelation friendRelation = new FriendRelation(
        friendInvitation.getRequestUserId().toString(),
        friendInvitation.getInvitedUserId().toString(),
        friendInvitation.getId().toString()
      );
      this.friendRelationRepository.save(friendRelation);
    }

    return new FriendInvitationDto(friendInvitation);
  }

  public GetFriendRelationWithNoMessagesResponseDto findWithNoChatMessages(int page, int size, String userId, String search) {
    return this.friendRelationRepositoryImpl.findWithNoChatMessages(
      page, size, UUID.fromString(userId), search
    );
  }
}
