package com.dev.discusso.module.chatMessage;

import com.dev.discusso.dto.requestSpecific.GetChatMessageListResponseDto;
import com.dev.discusso.dto.requestSpecific.GetChatMessageListResponseDto.GetChatMessageItemDto;
import com.dev.discusso.dto.requestSpecific.GetLatestChatMessageListResponseDto;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import com.dev.discusso.dto.requestSpecific.GetLatestChatMessageListResponseDto.GetLatestChatMessageItemDto;

import java.util.UUID;

@Repository
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom {
  private final EntityManager entityManager;

  public ChatMessageRepositoryImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public GetChatMessageListResponseDto findChatMessageList(
    UUID userId,
    UUID friendId,
    int page,
    int size
  ) {
    String query = """
        SELECT cm, u.username AS friendName\s
        FROM ChatMessage cm\s
        JOIN User u ON u.id = :friendId\s
        WHERE (cm.senderId = :friendId OR cm.receiverId = :friendId) AND\s
        (cm.senderId = :userId OR cm.receiverId = :userId)\s
        ORDER BY cm.timestamp DESC\s
      """;

    String countQuery = """
      SELECT COUNT(messages.messageId) AS count\s
      FROM (\s
        SELECT cm.id as messageId\s
        FROM ChatMessage cm\s
        JOIN User u ON u.id = :friendId\s
        WHERE (cm.senderId = :friendId OR cm.receiverId = :friendId) AND\s
        (cm.senderId = :userId OR cm.receiverId = :userId)\s
        ORDER BY cm.timestamp DESC\s
      ) AS messages\s""";

    GetChatMessageItemDto[] result = this.entityManager
      .createQuery(query, GetChatMessageListResponseDto.GetChatMessageItemDto.class)
      .setParameter("userId", userId)
      .setParameter("friendId", friendId)
      .setFirstResult(page * size)
      .setMaxResults(size)
      .getResultList()
      .toArray(new GetChatMessageListResponseDto.GetChatMessageItemDto[0]);

    Long totalCount = entityManager.createQuery(countQuery, Long.class)
      .setParameter("userId", userId)
      .setParameter("friendId", friendId)
      .getSingleResult();

    return new GetChatMessageListResponseDto(page, size, totalCount.intValue(), result);
  }

  public GetLatestChatMessageListResponseDto findLatestChatMessageList(
    UUID userId,
    int page,
    int size,
    String searchUsername
  ) {
    String query = """
        SELECT cm, u1.username AS senderName\s
        FROM ChatMessage cm\s
        JOIN User u ON u.id = cm.senderId OR u.id = cm.receiverId\s
        JOIN User u1 ON u.id != u1.id AND (u1.id = cm.senderId OR u1.id = cm.receiverId)\s
        LEFT JOIN (\s
          SELECT id as latestMessageId, c.senderId AS cSenderId, c.receiverId AS cReceiverId,\s
            ROW_NUMBER() OVER (PARTITION BY\s
              CASE WHEN c.senderId < c.receiverId THEN c.senderId ELSE c.receiverId END,\s
              CASE WHEN c.senderId < c.receiverId THEN c.receiverId ELSE c.senderId END\s
            ORDER BY c.timestamp DESC) AS rn\s
          FROM ChatMessage c\s
        ) lm ON\s
          (lm.cSenderId = u.id OR lm.cReceiverId = u.id) AND\s
          (lm.cSenderId = u1.id OR lm.cReceiverId = u1.id) AND\s
          (lm.latestMessageId = cm.id)
        WHERE u.id = :userId AND u1.username LIKE :searchUsername AND lm.rn = 1\s
        ORDER BY cm.timestamp DESC\s
      """;

    String countQuery = """
      SELECT COUNT(latestMessages.senderId) AS count\s
      FROM (\s
        SELECT u.id AS senderId\s
        FROM ChatMessage cm\s
        JOIN User u ON u.id = cm.senderId OR u.id = cm.receiverId\s
        JOIN User u1 ON u.id != u1.id AND (u1.id = cm.senderId OR u1.id = cm.receiverId)\s
        LEFT JOIN (\s
          SELECT id as latestMessageId, c.senderId AS cSenderId, c.receiverId AS cReceiverId,\s
            ROW_NUMBER() OVER (PARTITION BY\s
              CASE WHEN c.senderId < c.receiverId THEN c.senderId ELSE c.receiverId END,\s
              CASE WHEN c.senderId < c.receiverId THEN c.receiverId ELSE c.senderId END\s
            ORDER BY c.timestamp DESC) AS rn\s
          FROM ChatMessage c\s
        ) lm ON\s
          (lm.cSenderId = u.id OR lm.cReceiverId = u.id) AND\s
          (lm.cSenderId = u1.id OR lm.cReceiverId = u1.id) AND\s
          (lm.latestMessageId = cm.id)
        WHERE u.id = :userId AND u1.username LIKE :searchUsername AND lm.rn = 1\s
        ORDER BY cm.timestamp DESC\s
      ) AS latestMessages\s""";

    GetLatestChatMessageItemDto[] result = this.entityManager
      .createQuery(query, GetLatestChatMessageItemDto.class)
      .setParameter("userId", userId)
      .setParameter("searchUsername", searchUsername + "%")
      .setFirstResult(page * size)
      .setMaxResults(size)
      .getResultList()
      .toArray(new GetLatestChatMessageItemDto[0]);

    Long totalCount = entityManager.createQuery(countQuery, Long.class)
      .setParameter("userId", userId)
      .setParameter("searchUsername", searchUsername + "%")
      .getSingleResult();

    return new GetLatestChatMessageListResponseDto(page, size, totalCount.intValue(), result);
  }
}
