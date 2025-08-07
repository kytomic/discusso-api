package com.dev.discusso.module.friendRelation;

import com.dev.discusso.dto.requestSpecific.GetChatMessageListResponseDto;
import com.dev.discusso.dto.requestSpecific.GetFriendRelationWithNoMessagesResponseDto;
import com.dev.discusso.dto.requestSpecific.GetFriendRelationWithNoMessagesResponseDto.GetFriendRelationWithNoMessagesItemDto;
import com.dev.discusso.schema.FriendInvitation;
import com.dev.discusso.schema.FriendRelation;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FriendRelationRepositoryImpl implements FriendRelationRepositoryCustom {
  private final EntityManager entityManager;

  FriendRelationRepositoryImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public Optional<FriendRelation> findByUserId1AndUserId2(UUID userId1, UUID userId2) {
    try {
      return Optional.of(
        this.entityManager.createQuery(
            "SELECT fr FROM FriendRelation fr WHERE (fr.userId1 = :userId1 AND fr.userId2 = :userId2) OR (fr.userId1 = :userId2 AND fr.userId2 = :userId1)",
            FriendRelation.class
          )
          .setParameter("userId1", userId1)
          .setParameter("userId2", userId2)
          .getSingleResult());
    } catch (NoResultException err) {
      return Optional.empty();
    }
  }

  public Optional<FriendInvitation> findByRequestUserIdAndInvitedUsername(UUID requestUserId, String invitedUserDisplayName) {
    try {
      return Optional.of(
        this.entityManager.createQuery(
            "SELECT fi FROM FriendInvitation fi WHERE fi.requestUserId = :requestUserId AND fi.invitedUsername = :invitedUsername",
            FriendInvitation.class
          )
          .setParameter("requestUserId", requestUserId)
          .setParameter("invitedUsername", invitedUserDisplayName)
          .getSingleResult());
    } catch (NoResultException err) {
      return Optional.empty();
    }
  }

  public GetFriendRelationWithNoMessagesResponseDto findWithNoChatMessages(
    int page,
    int size,
    UUID userId,
    String search
  ) {
    String query = """
        SELECT fr, u.displayName AS friendName\s
        FROM FriendRelation fr\s
        JOIN User u ON (u.id = fr.userId1 OR u.id = fr.userId2)\s
        LEFT JOIN ChatMessage cm ON\s
          (cm.senderId = fr.userId1 OR cm.receiverId = fr.userId1) AND\s
          (cm.senderId = fr.userId2 OR cm.receiverId = fr.userId2)\s
        WHERE cm.id IS NULL AND\s
          (fr.userId1 = :userId OR fr.userId2 = :userId) AND\s
          u.id != :userId AND\s
          u.displayName LIKE :search\s
      """;

    String countQuery = """
        SELECT COUNT(friendRelationId) AS count\s
        FROM (\s
          SELECT fr.id AS friendRelationId\s
          FROM FriendRelation fr\s
          JOIN User u ON (u.id = fr.userId1 OR u.id = fr.userId2)\s
          LEFT JOIN ChatMessage cm ON\s
            (cm.senderId = fr.userId1 OR cm.receiverId = fr.userId1) AND\s
            (cm.senderId = fr.userId2 OR cm.receiverId = fr.userId2)\s
          WHERE cm.id IS NULL AND\s
            (fr.userId1 = :userId OR fr.userId2 = :userId) AND\s
            u.id != :userId AND\s
            u.displayName LIKE :search\s
        ) AS friendRelations\s
      """;

    GetFriendRelationWithNoMessagesItemDto[] result = this.entityManager
      .createQuery(query, GetFriendRelationWithNoMessagesItemDto.class)
      .setParameter("userId", userId)
      .setParameter("search", search + "%")
      .setFirstResult(page * size)
      .setMaxResults(size)
      .getResultList()
      .toArray(new GetFriendRelationWithNoMessagesItemDto[0]);

    Long totalCount = entityManager.createQuery(countQuery, Long.class)
      .setParameter("userId", userId)
      .setParameter("search", search + "%")
      .getSingleResult();

    return new GetFriendRelationWithNoMessagesResponseDto(page, size, totalCount.intValue(), result);
  }
}
