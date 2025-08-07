package com.dev.discusso.module.friendRelation;

import com.dev.discusso.schema.FriendRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FriendRelationRepository extends JpaRepository<FriendRelation, UUID> {
  public FriendRelation save(FriendRelation friendRelation);
}
