package paoo.cappuccino.business.dto;

import java.time.LocalDateTime;

import paoo.cappuccino.util.hasher.IHashHolderDto;

/**
 * Data transfer object for the User entity.
 *
 * @author Nicolas Fischer
 */
public interface IUserDto extends IBaseDto {

  /**
   * Gets the user's username.
   */
  String getUsername();

  /**
   * Gets the user's password.
   */
  IHashHolderDto getPassword();

  /**
   * Gets the user's last name.
   */
  String getLastName();

  /**
   * Gets the user's first name.
   */
  String getFirstName();

  /**
   * Gets the user's email.
   */
  String getEmail();

  /**
   * Gets the user's register date.
   */
  LocalDateTime getRegisterDate();

  /**
   * Gets the user's role within the application. The role is used to define the list of actions the
   * user can do.
   */
  IUserDto.Role getRole();

  /**
   * Compares the given password to the one stored by the user entity.
   * @param password The password to compare.
   * @return The password matches.
   */
  boolean isPassword(char[] password);

  /**
   * Defines the list of roles an user can hold.
   */
  enum Role {
    USER, ADMIN
  }
}
