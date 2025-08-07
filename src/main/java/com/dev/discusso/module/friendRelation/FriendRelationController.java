package com.dev.discusso.module.friendRelation;

import com.dev.discusso.common.exception.CustomException;
import com.dev.discusso.dto.FriendInvitationDto;
import com.dev.discusso.dto.FriendRelationDto;
import com.dev.discusso.dto.UserDto;
import com.dev.discusso.dto.requestSpecific.*;
import com.dev.discusso.module.user.UserService;
import com.dev.discusso.schema.FriendRelation;
import com.dev.discusso.schema.UserDetailInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/friend")
public class FriendRelationController {
  private final FriendRelationService friendRelationService;
  private final UserService userService;

  public FriendRelationController(FriendRelationService friendRelationService, UserService userService) {
    this.friendRelationService = friendRelationService;
    this.userService = userService;
  }

  @GetMapping("/pendingInvitations")
  public ResponseEntity<GetPendingFriendInvitationListResponseDto> getFriendInvitationList(
    @RequestParam("page") int page,
    @RequestParam("pageSize") int pageSize,
    @AuthenticationPrincipal UserDto userDto
  ) {
    String invitedUserId = userDto.getId();
    GetPendingFriendInvitationListRequestDto requestDto = new GetPendingFriendInvitationListRequestDto(invitedUserId, page, pageSize);
    GetPendingFriendInvitationListResponseDto response = this.friendRelationService.getFriendInvitationList(requestDto);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/invite")
  public ResponseEntity<CreateFriendInvitationResponseDto> createFriendInvitation(
    @RequestBody CreateFriendInvitationRequestDto dto,
    @AuthenticationPrincipal UserDto userDto
  ) {
    dto.setRequestUserId(userDto.getId());
    CreateFriendInvitationResponseDto result = this.friendRelationService.createFriendInvitation(dto);
    return ResponseEntity.ok(result);
  }

  @PutMapping("/acceptInvitation/{invitationId}")
  public ResponseEntity<FriendInvitationDto> updateInvitation(
    @PathVariable("invitationId") String invitationId,
    @RequestBody UpdateFriendInvitationRequestDto dto
  ) {
    FriendInvitationDto result = this.friendRelationService.updateInvitation(invitationId, dto.isAccepted());
    return ResponseEntity.ok(result);
  }

  @GetMapping("/noMessages")
  public ResponseEntity<GetFriendRelationWithNoMessagesResponseDto> getFriendRelationWithNoMessages(
    @RequestParam("page") int page,
    @RequestParam("pageSize") int pageSize,
    @RequestParam("search") String search,
    @AuthenticationPrincipal UserDto userDto
  ) {
    String userId = userDto.getId();
    return ResponseEntity.ok(this.friendRelationService.findWithNoChatMessages(
      page, pageSize, userId, search
    ));
  }
}
