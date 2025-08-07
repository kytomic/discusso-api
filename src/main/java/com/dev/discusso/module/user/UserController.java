package com.dev.discusso.module.user;

import com.dev.discusso.common.exception.CustomException;
import com.dev.discusso.dto.UserDto;
import com.dev.discusso.dto.requestSpecific.RefreshUserRequestDto;
import com.dev.discusso.dto.requestSpecific.RefreshUserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/api/user")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDto> getUserInfo(@PathVariable("id") String id) {
    Optional<UserDto> userDto = userService.findById(id);
    if (userDto.isEmpty())
      throw new CustomException("0010_USER_NOT_FOUND");

    return ResponseEntity.ok(userDto.get());
  }

  @PostMapping("/signUp")
  public ResponseEntity<UserDto> signUp(@RequestBody UserDto userDto) {
    UserDto createdDto = userService.create(userDto);
    return ResponseEntity.ok(createdDto);
  }

  @PostMapping("/login")
  public ResponseEntity<UserDto> login(@RequestBody UserDto userDto) {
    UserDto loggedInUser = userService.login(userDto);
    return ResponseEntity.ok(loggedInUser);
  }

  @PostMapping("/refresh")
  public ResponseEntity<RefreshUserResponseDto> refresh(@RequestBody RefreshUserRequestDto dto) {
    return ResponseEntity.ok(userService.verifyRefreshToken(dto));
  }
}
