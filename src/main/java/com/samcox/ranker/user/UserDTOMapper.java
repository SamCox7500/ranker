package com.samcox.ranker.user;

import java.util.List;
import java.util.stream.Collectors;

public class UserDTOMapper {

  public static UserDTO toUserDTO(User user) {
    return new UserDTO(user.getId(), user.getUsername());
  }
  public static List<UserDTO> toUserDTOs(List<User> users) {
    return users.stream()
      .map(UserDTOMapper::toUserDTO)
      .collect(Collectors.toList());
  }
}
