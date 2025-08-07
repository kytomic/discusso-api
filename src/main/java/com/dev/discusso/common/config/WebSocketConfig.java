package com.dev.discusso.common.config;

import com.dev.discusso.common.filter.WebSocketHandshakeHandler;
import com.dev.discusso.dto.UserDto;
import com.dev.discusso.dto.UserPrincipalDto;
import com.dev.discusso.module.auth.JWTService;
import com.dev.discusso.module.auth.UserDetailInfoService;
import com.dev.discusso.module.user.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  @Value("${frontend.host}")
  private String frontendHost;
  private final ApplicationContext context;

  // ✅ Constructor injection
  public WebSocketConfig(ApplicationContext context) {
    this.context = context;
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/topic", "/queue");
    config.setApplicationDestinationPrefixes("/app");
    config.setUserDestinationPrefix("/user");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws").setAllowedOriginPatterns(frontendHost, "*");
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(new ChannelInterceptor() {
      @Override
      public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        System.out.println("PreSend: " + accessor.getCommand().toString());
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
          List<String> authHeaders = accessor.getNativeHeader("Authorization");
          String authHeader = authHeaders.get(0);
          String token = null;
          String username = null;

          if (authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = context.getBean(JWTService.class).extractUsername(token);
          }

          if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = context.getBean(UserDetailInfoService.class).loadUserByUsername(username);

            if (context.getBean(JWTService.class).validateToken(token, userDetails)) {
              UserDto userDto = context.getBean(UserService.class).findByUsername(username).orElse(null);
              UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDto, null, userDetails.getAuthorities());
              SecurityContextHolder.getContext().setAuthentication(authToken);
              System.out.println("WebSocket authenticated as: " + username);
              accessor.setUser(new UserPrincipalDto(username));
            }
          }
        }
        return message;
      }
    });
  }
}
