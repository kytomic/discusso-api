package com.dev.discusso.module.user;

import com.dev.discusso.dto.UserDto;
import com.dev.discusso.schema.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepositoryCustom {
  private EntityManager entityManager;
  private PasswordEncoder passwordEncoder;

  public UserRepositoryImpl(EntityManager entityManager, PasswordEncoder passwordEncoder) {
    this.entityManager = entityManager;
    this.passwordEncoder = passwordEncoder;
  }
}
