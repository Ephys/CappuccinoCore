package paoo.cappuccino.dal.exception;

import paoo.cappuccino.util.exception.CappuccinoException;

/**
 * Base exception for problems occurring on the data access layer of the application.
 *
 * @author Guylian Cox
 */
public abstract class DalException extends CappuccinoException {

  private static final long serialVersionUID = 7915439352322904989L;

  public DalException(String message) {
    super(message);
  }

  public DalException(String message, Throwable cause) {
    super(message, cause);
  }
}
