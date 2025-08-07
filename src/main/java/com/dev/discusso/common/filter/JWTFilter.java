package com.dev.discusso.common.filter;

import com.dev.discusso.common.exception.CustomException;
import com.dev.discusso.dto.UserDto;
import com.dev.discusso.module.auth.JWTService;
import com.dev.discusso.module.auth.UserDetailInfoService;
import com.dev.discusso.module.user.UserRepository;
import com.dev.discusso.module.user.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
  private JWTService jwtService;
  @Autowired
  private ApplicationContext context;

  public JWTFilter(JWTService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");
    String token = null;
    String username = null;
    String path = request.getRequestURI();

    if (path.startsWith("/ws")) {
      filterChain.doFilter(request, response);
      return;
    }

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      token = authHeader.substring(7);
      username = jwtService.extractUsername(token);
    }

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = context.getBean(UserDetailInfoService.class).loadUserByUsername(username);
      if (jwtService.validateToken(token, userDetails)) {
        UserDto userDto = context.getBean(UserService.class).findByUsername(username).orElse(null);
        // Create authentication object
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDto, null, userDetails.getAuthorities());
        // Collect user information from web request
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // Store authentication object in security context
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }

    filterChain.doFilter(request, response);
  }
}
