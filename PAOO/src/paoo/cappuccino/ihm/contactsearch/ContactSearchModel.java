package paoo.cappuccino.ihm.contactsearch;

import paoo.cappuccino.ihm.util.BaseModel;


public class ContactSearchModel extends BaseModel {
  private String firstName, lastName;

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
    dispatchChangeEvent();
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
    dispatchChangeEvent();
  }



}
