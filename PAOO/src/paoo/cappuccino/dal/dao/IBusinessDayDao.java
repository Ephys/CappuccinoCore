package paoo.cappuccino.dal.dao;

import paoo.cappuccino.business.dto.IBusinessDayDto;

public interface IBusinessDayDao {

  /**
   * Inserts a new business day in the database.
   *
   * @param businessDay The business day to insert.
   * @return The business day entity with its information updated from the database.
   * @throws paoo.cappuccino.dal.exception.NonUniqueFieldException The is already a business day during that academic year
   * @throws java.lang.IllegalArgumentException                    One of the fields failed to
   *                                                               insert due to constraint
   *                                                               violations.
   * @throws paoo.cappuccino.dal.exception.ConnectionException     Database connection error.
   */
  IBusinessDayDto createBusinessDay(IBusinessDayDto businessDay);

  IBusinessDayDto[] fetchAll();
  IBusinessDayDto[] fetchInvitationlessDays();

  /**
   * Fetch the BusinessDay corresponding at the date.
   *
   * @param date The date of the business day
   * @return The business day or null if none was found.
   * @throws paoo.cappuccino.dal.exception.ConnectionException Database connection error
   */
  //IBusinessDaysDto fetchBusinessDaysByDate(int year);
}