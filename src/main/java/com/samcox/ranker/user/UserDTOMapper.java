package com.samcox.ranker.user;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class for mapping a {@link User} to a {@link UserDTO}
 *
 * @see User
 * @see UserDTO
 */
public class UserDTOMapper {

  /**
   * Static method for creating a {@link UserDTO} object from a {@link User} object
   * @param user the user object to create a UserDTO from
   * @return the UserDTO generated from the user object
   */
  public static UserDTO toUserDTO(User user) {
    return new UserDTO(user.getId(), user.getUsername());
  }

  /**
   * Static method for creating a list of {@link UserDTO} objects from a list of {@link User} objects
   * @param users the list of user objects to be used in creating a list of UserDTOs
   * @return the list of UserDTO objects
   */
  public static List<UserDTO> toUserDTOs(List<User> users) {
    return users.stream()
      .map(UserDTOMapper::toUserDTO)
      .collect(Collectors.toList());
  }
}
