package paoo.cappuccino.business.entity.impl;

import java.time.LocalDateTime;

import paoo.cappuccino.business.dto.IContactDto;
import paoo.cappuccino.business.dto.IParticipationDto;
import paoo.cappuccino.business.entity.ICompany;
import paoo.cappuccino.business.entity.IUser;

public class Company extends BaseEntity implements ICompany {

  public Company(int id) {
    super(id);
    // TODO Auto-generated constructor stub
  }

  @Override
  public String getName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public LocalDateTime getRegisterDate() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getAddressStreet() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getAddressNum() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getAddressMailbox() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getAddressPostcode() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getAddressTown() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IUser GetCreator() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IParticipationDto[] getParticipations() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void addContact(IContactDto contact) {
    // TODO Auto-generated method stub

  }

}
