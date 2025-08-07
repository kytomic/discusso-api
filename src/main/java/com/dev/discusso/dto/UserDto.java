package com.dev.discusso.dto;

import com.dev.discusso.schema.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends BaseDto {
  private String id;
  private String username;
  private String password;
  private String email;
  private String displayName;

  private String accessToken;
  private String refreshToken;

  public UserDto(String username, String password, String email, String createdBy, String updatedBy) {
    super(createdBy, createdBy);
    this.username = username;
    this.password = password;
    this.email = email;
  }

  public UserDto(User user) {
    super(user.getCreatedBy(), user.getUpdatedBy());
    this.setId(user.getId().toString());
    this.setUsername(user.getUsername());
    this.setEmail(user.getEmail());
    this.setDisplayName(user.getDisplayName());
    this.setCreatedAt(user.getCreatedAt());
    this.setUpdatedAt(user.getUpdatedAt());
    this.setDeleted(user.isDeleted());
  }
}
