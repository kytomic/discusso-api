package com.dev.discusso.schema;

import com.dev.discusso.dto.UserDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "user", schema = "public")
public class User extends BaseModel {
  @Id
  @GeneratedValue
  @Column(columnDefinition = "uuid", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "username", nullable = false, unique = true)
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "display_name", nullable = false)
  private String displayName;

  public User(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }

  public User(UserDto userDto) {
    this.username = userDto.getUsername();
    this.password = userDto.getPassword();
    this.email = userDto.getEmail();
    this.displayName = userDto.getUsername();
    this.setCreatedAt(LocalDateTime.now());
    this.setCreatedBy("system");
    this.setUpdatedAt(LocalDateTime.now());
    this.setUpdatedBy("system");
  }
}
