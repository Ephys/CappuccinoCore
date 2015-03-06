package paoo.cappuccino.business.entity;

import paoo.cappuccino.business.dto.IBaseDto;

/**
 * Interface containing methods shared by every entity
 *
 * @author Nicolas Fischer
 */
public interface IBaseEntity extends IBaseDto {

  /**
   * Increments and returns the entity version
   */
  int incrementVersion();
}
