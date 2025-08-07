package com.dev.discusso.module.user;

import com.dev.discusso.schema.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
  public Optional<User> findById(UUID id);

  public Optional<User> findByUsername(String username);

  public Optional<User> findByDisplayName(String displayName);

  public User save(User user);
}
