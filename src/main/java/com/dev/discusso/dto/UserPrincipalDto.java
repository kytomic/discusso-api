package com.dev.discusso.dto;

import java.security.Principal;

public class UserPrincipalDto implements Principal {
  private final String name;// the username

  public UserPrincipalDto(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return this.name;
  }
}