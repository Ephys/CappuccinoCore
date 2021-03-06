package paoo.cappuccino.ucc;

import paoo.cappuccino.business.dto.IUserDto;

/**
 * Use case controller containing methods relative to an user (as en entity, not as an actor).
 *
 * @author Guylian Cox
 */
public interface IUserUcc {

  /**
   * Registers an user in the system
   *
   * @param username  The user's username. Used for logging in, must be unique and is
   *                  case-insensitive.
   * @param password  The user's password.
   * @param firstName The user's first name.
   * @param lastName  The user's last name.
   * @param email     A valid email.
   * @return The registered user's DTO
   * @throws java.lang.IllegalArgumentException The username isn't unique, the email is not valid or
   *                                            the username contains spaces.
   */
  IUserDto register(String username, char[] password, String firstName, String lastName,
                    String email);

  /**
   * Logs an user into the system.
   *
   * @param username The user's username. The input is case-insensitive.
   * @param password The user's password.
   * @return The logged user's DTO or null if the username/password combination is incorrect.
   * @throws java.lang.IllegalArgumentException A parameter is null or empty.
   */
  IUserDto logIn(String username, char[] password);

  /**
   * Fetches the user matching a given id.
   *
   * @param id The user identifier.
   * @return The user or null if none matched.
   */
  IUserDto getUserById(int id);
}