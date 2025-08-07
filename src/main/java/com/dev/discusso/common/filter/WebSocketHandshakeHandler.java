package com.dev.discusso.common.filter;

import com.dev.discusso.dto.UserDto;
import com.dev.discusso.module.auth.JWTService;
import com.dev.discusso.module.auth.UserDetailInfoService;
import com.dev.discusso.module.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Enumeration;
import java.util.Map;

public class WebSocketHandshakeHandler extends DefaultHandshakeHandler {
  private JWTService jwtService;
  private UserDetailInfoService userDetailInfoService;
  private UserService userService;

  @Override
  protected Principal determineUser(
      ServerHttpRequest request,
      WebSocketHandler webSocketHandler,
      Map<String, Object> attributes
  ) {
    if (request instanceof ServletServerHttpRequest servletRequest) {
      HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
      String authHeader = httpServletRequest.getHeader("Authorization");

      Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
      while (headerNames.hasMoreElements()) {
        String headerName = headerNames.nextElement();
        String headerValue = httpServletRequest.getHeader(headerName);
        System.out.println(headerName + ": " + headerValue);
      }

      String username = null;
      String token = null;

      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        token = authHeader.substring(7);
        username = jwtService.extractUsername(token);
      }

      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = userDetailInfoService.loadUserByUsername(username);
        if (jwtService.validateToken(token, userDetails)) {
          UserDto userDto = userService.findByUsername(username).orElse(null);
          // Create authentication object
          return new UsernamePasswordAuthenticationToken(userDto, null, userDetails.getAuthorities());
        }
      }
    }
    return null;
  }
}