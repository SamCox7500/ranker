package com.samcox.ranker;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrentUserController {
  @GetMapping("/authuser")
  public UserInfo getAuthUser() {
    Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
    return new UserInfo(currentAuth.getName());
  }
}
