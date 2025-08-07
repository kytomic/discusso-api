package com.dev.discusso.module.user;

import com.dev.discusso.dto.UserDto;
import com.dev.discusso.dto.requestSpecific.RefreshUserRequestDto;
import com.dev.discusso.dto.requestSpecific.RefreshUserResponseDto;

import java.util.Optional;

public interface UserService {
  public Optional<UserDto> findById(String id);

  public Optional<UserDto> findByUsername(String username);

  public UserDto create(UserDto userDTO);

  public UserDto login(UserDto userDTO);

  public RefreshUserResponseDto verifyRefreshToken(RefreshUserRequestDto dto);
}
