package com.dev.discusso.module.user;

import com.dev.discusso.common.exception.CustomException;
import com.dev.discusso.dto.UserDto;
import com.dev.discusso.dto.requestSpecific.RefreshUserRequestDto;
import com.dev.discusso.dto.requestSpecific.RefreshUserResponseDto;
import com.dev.discusso.module.auth.JWTService;
import com.dev.discusso.module.auth.UserDetailInfoService;
import com.dev.discusso.schema.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
  private UserRepository userRepository;
  private PasswordEncoder bcryptEncoder;

  private final JWTService jwtService;
  private final UserDetailInfoService userDetailInfoService;

  public UserServiceImpl(UserRepository userRepository,
                         PasswordEncoder bcryptEncoder,
                         JWTService jwtService,
                         UserDetailInfoService userDetailInfoService) {
    this.userRepository = userRepository;
    this.bcryptEncoder = bcryptEncoder;
    this.jwtService = jwtService;
    this.userDetailInfoService = userDetailInfoService;
  }

  @Override
  public Optional<UserDto> findById(String id) {
    User user = userRepository.findById(UUID.fromString(id)).orElse(null);
    if (user == null)
      return Optional.empty();

    return Optional.of(new UserDto(user));
  }

  @Override
  public Optional<UserDto> findByUsername(String username) {
    User user = userRepository.findByUsername(username).orElse(null);
    if (user == null)
      return Optional.empty();

    return Optional.of(new UserDto(user));
  }


  @Override
  public UserDto create(UserDto userDTO) throws CustomException {
    Optional<User> foundUser = userRepository.findByUsername(userDTO.getUsername());
    if (foundUser.isPresent()) {
      throw new CustomException("0012_USERNAME_ALREADY_EXISTS");
    }

    userDTO.setPassword(bcryptEncoder.encode(userDTO.getPassword()));
    User user = userRepository.save(new User(userDTO));
    return new UserDto(user);
  }

  @Override
  public UserDto login(UserDto userDTO) throws CustomException {
    Optional<User> foundUser = userRepository.findByUsername(userDTO.getUsername());
    if (foundUser.isEmpty() || !bcryptEncoder.matches(userDTO.getPassword(), foundUser.get().getPassword())) {
      throw new CustomException("0013_INVALID_CREDENTIALS");
    }
    String accessToken = jwtService.generateToken(foundUser.get().getUsername(), true);
    String refreshToken = jwtService.generateToken(foundUser.get().getUsername(), false);

    UserDto userDto = new UserDto(foundUser.get());
    userDto.setAccessToken(accessToken);
    userDto.setRefreshToken(refreshToken);
    return userDto;
  }

  @Override
  public RefreshUserResponseDto verifyRefreshToken(RefreshUserRequestDto dto) {
    String refreshToken = dto.getRefreshToken();
    String username = jwtService.extractUsername(dto.getRefreshToken());
    UserDetails userDetails = userDetailInfoService.loadUserByUsername(username);

    if (username == null || !jwtService.validateToken(refreshToken, userDetails)) {
      throw new CustomException("0013_INVALID_REFRESH_TOKEN");
    }

    String accessToken = jwtService.generateToken(username, true);
    return new RefreshUserResponseDto(accessToken);
  }
}
