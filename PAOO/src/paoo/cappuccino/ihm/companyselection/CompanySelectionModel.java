package paoo.cappuccino.ihm.companyselection;

import paoo.cappuccino.business.dto.ICompanyDto;
import paoo.cappuccino.ihm.util.BaseModel;

public class CompanySelectionModel extends BaseModel {
  private ICompanyDto[] companyDto;
  boolean selectAll;

  public void setCompanyDto(ICompanyDto[] companyDto) {
    this.companyDto = companyDto;
    this.selectAll = false;
    dispatchChangeEvent();
  }

  public ICompanyDto[] getCompanyDto() {

    return this.companyDto;
  }

  public void setSelectAll(boolean selectAll) {
    this.selectAll = selectAll;
    dispatchChangeEvent();
  }

  public boolean getSelectAll() {
    return this.selectAll;
  }
}