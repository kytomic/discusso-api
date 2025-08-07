package com.dev.discusso.module.chatMessage;

import com.dev.discusso.common.exception.CustomException;
import com.dev.discusso.dto.ChatMessageDto;
import com.dev.discusso.dto.UserDto;
import com.dev.discusso.dto.requestSpecific.GetChatMessageListResponseDto;
import com.dev.discusso.dto.requestSpecific.GetLatestChatMessageListResponseDto;
import com.dev.discusso.module.user.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;


@Data
@Controller
@RequestMapping("/api/chat")
public class ChatMessageController {
  private final SimpMessagingTemplate messagingTemplate;
  private final ChatMessageService chatMessageService;
  private final UserService userService;

  @Autowired
  private SimpUserRegistry simpUserRegistry;

  public ChatMessageController(
    SimpMessagingTemplate messagingTemplate,
    ChatMessageService chatMessageService,
    UserService userService
  ) {
    this.messagingTemplate = messagingTemplate;
    this.chatMessageService = chatMessageService;
    this.userService = userService;
  }

  @MessageMapping("/chat")
  public void handleMessage(ChatMessageDto chatMessageDto, Principal principal) {
    String username = principal.getName();
    Optional<UserDto> userDto = this.userService.findByUsername(username);
    if (userDto.isEmpty()) {
      throw new CustomException("0010_USER_NOT_FOUND1");
    }

    Optional<UserDto> receiverDto = this.userService.findById(chatMessageDto.getReceiverId());
    if (receiverDto.isEmpty()) {
      throw new CustomException("0010_USER_NOT_FOUND");
    }

    chatMessageDto.setSenderId(userDto.get().getId());
    ChatMessageDto savedMessageDto = chatMessageService.create(chatMessageDto);
    messagingTemplate.convertAndSendToUser(receiverDto.get().getUsername(), "/queue/messages", savedMessageDto);
  }

  @GetMapping("")
  public ResponseEntity<GetChatMessageListResponseDto> getChatMessageList(
    @RequestParam("page") int page,
    @RequestParam("pageSize") int pageSize,
    @RequestParam("friendId") String friendId,
    @AuthenticationPrincipal UserDto userDto
  ) {
    String userId = userDto.getId();
    return ResponseEntity.ok(this.chatMessageService.getChatMessageList(userId, friendId, page, pageSize));
  }

  @GetMapping("/latest")
  public ResponseEntity<GetLatestChatMessageListResponseDto> getLatestChatMessageList(
    @RequestParam("page") int page,
    @RequestParam("pageSize") int pageSize,
    @RequestParam("search") String searchUsername,
    @AuthenticationPrincipal UserDto userDto
  ) {
    String userId = userDto.getId();
    return ResponseEntity.ok(this.chatMessageService.getLatestChatMessageList(userId, page, pageSize, searchUsername));
  }
}
