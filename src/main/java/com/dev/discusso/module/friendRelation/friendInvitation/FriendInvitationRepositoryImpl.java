package com.dev.discusso.module.friendRelation.friendInvitation;

import com.dev.discusso.dto.requestSpecific.GetPendingFriendInvitationListResponseDto;
import com.dev.discusso.dto.requestSpecific.GetPendingFriendInvitationListResponseDto.GetFriendInvitationResponseItem;
import com.dev.discusso.schema.FriendInvitation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class FriendInvitationRepositoryImpl implements FriendInvitationRepositoryCustom {
  private final EntityManager entityManager;

  public FriendInvitationRepositoryImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public GetPendingFriendInvitationListResponseDto findByInvitedUserIdAndIsPendingWithPagination(
      UUID invitedUserId, boolean isPending, int page, int size
  ) {
    String query =
        "SELECT i, u.username AS invitedUserDisplayName FROM FriendInvitation i " +
            "JOIN User u ON u.id = i.requestUserId " +
            "WHERE i.invitedUserId = :invitedUserId AND isPending = :isPending AND i.isDeleted = false";
    String countQuery =
        "SELECT COUNT(i) FROM FriendInvitation i " +
            "JOIN User u ON u.id = i.requestUserId " +
            "WHERE i.invitedUserId = :invitedUserId AND isPending = :isPending AND i.isDeleted = false";

    GetFriendInvitationResponseItem[] result = this.entityManager
        .createQuery(query, GetFriendInvitationResponseItem.class)
        .setParameter("invitedUserId", invitedUserId)
        .setParameter("isPending", isPending)
        .setFirstResult(page * size)
        .setMaxResults(size)
        .getResultList()
        .toArray(new GetFriendInvitationResponseItem[0]);

    Long totalCount = entityManager.createQuery(countQuery, Long.class)
        .setParameter("invitedUserId", invitedUserId)
        .setParameter("isPending", isPending)
        .getSingleResult();

    return new GetPendingFriendInvitationListResponseDto(page, size, totalCount.intValue(), result);
  }

  public Optional<FriendInvitation> findByRequestUserIdAndInvitedUsername(UUID requestUserId, String invitedUserDisplayName) {
    String query =
        "SELECT i FROM FriendInvitation i " +
            "JOIN User u ON u.id = i.requestUserId " +
            "WHERE i.requestUserId = :requestUserId AND i.isDeleted = false AND u.displayName = :invitedUserDisplayName";

    try {
      return Optional.of(
          this.entityManager.createQuery(query, FriendInvitation.class)
              .setParameter("requestUserId", requestUserId)
              .setParameter("invitedUserDisplayName", invitedUserDisplayName)
              .getSingleResult()
      );
    } catch (NoResultException e) {
      return Optional.empty();
    } catch (Exception e) {
      throw new RuntimeException("Error while fetching friend invitation by request user ID and invited username", e);
    }
  }
}
