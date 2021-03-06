package paoo.cappuccino.dal;

/**
 * Interface responsible for handling transactions.
 *
 * @author Kevin Bavay
 */
public interface IDalService {

  /**
   * Starts a transaction.
   *
   * @throws java.lang.IllegalStateException This thread already has a transaction is already in
   *                                         progress.
   */
  void startTransaction();

  /**
   * Closes the transaction by committing the changes.
   *
   * @throws java.lang.IllegalStateException This thread doesn't have a transaction running.
   */
  void commit();

  /**
   * Closes the transaction by rollbacking the changes.
   *
   * @throws java.lang.IllegalStateException This thread doesn't have a transaction running.
   */
  void rollback();

}
