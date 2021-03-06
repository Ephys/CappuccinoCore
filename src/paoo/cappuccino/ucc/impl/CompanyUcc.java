package paoo.cappuccino.ucc.impl;

import java.util.List;

import paoo.cappuccino.business.dto.ICompanyDto;
import paoo.cappuccino.business.dto.IParticipationDto;
import paoo.cappuccino.business.dto.IUserDto;
import paoo.cappuccino.business.entity.factory.IEntityFactory;
import paoo.cappuccino.core.injector.Inject;
import paoo.cappuccino.dal.dao.ICompanyDao;
import paoo.cappuccino.dal.dao.IParticipationDao;
import paoo.cappuccino.dal.exception.NonUniqueFieldException;
import paoo.cappuccino.ucc.ICompanyUcc;
import paoo.cappuccino.util.StringUtils;
import paoo.cappuccino.util.ValidationUtil;

class CompanyUcc implements ICompanyUcc {

  private final IEntityFactory factory;
  private final ICompanyDao companyDao;
  private final IParticipationDao participationDao;

  @Inject
  public CompanyUcc(IEntityFactory entityFactory, ICompanyDao companyDao,
      IParticipationDao participationDao) {
    this.factory = entityFactory;
    this.companyDao = companyDao;
    this.participationDao = participationDao;
  }

  @Override
  public ICompanyDto create(IUserDto creator, String name, String street, String numAddress,
      String mailBox, String postCode, String town) {
    ValidationUtil.ensureNotNull(creator, "creator");
    ValidationUtil.ensureFilled(name, "name");
    ValidationUtil.ensureFilled(street, "street");
    ValidationUtil.ensureFilled(numAddress, "numAddress");
    ValidationUtil.ensureFilled(postCode, "postCode");
    ValidationUtil.ensureFilled(town, "town");

    if (StringUtils.isEmpty(mailBox)) {
      mailBox = null;
    }

    ICompanyDto dto =
        factory.createCompany(creator.getId(), name, street, numAddress, mailBox, postCode, town);

    try {
      return companyDao.createCompany(dto);
    } catch (NonUniqueFieldException e) {
      throw new IllegalArgumentException("There is already a company registered with that name.");
    }
  }

  @Override
  public List<ICompanyDto> searchCompanies(String name, String poC, String town, String street) {
    if (StringUtils.isEmpty(street)) {
      street = null;
    }

    if (StringUtils.isEmpty(town)) {
      town = null;
    }

    if (StringUtils.isEmpty(poC)) {
      poC = null;
    }

    if (StringUtils.isEmpty(name)) {
      name = null;
    }

    return companyDao.searchCompanies(name, poC, street, town);
  }

  @Override
  public List<ICompanyDto> getInvitableCompanies() {
    return companyDao.fetchInvitableCompanies();
  }

  @Override
  public List<ICompanyDto> getAllCompanies() {
    return companyDao.fetchAll();
  }

  @Override
  public ICompanyDto getCompanyById(int company) {
    if (company <= 0) {
      throw new IllegalArgumentException("Invalid company id " + company);
    }

    return companyDao.fetchCompanyById(company);
  }

  @Override
  public List<ICompanyDto> getCompaniesByDay(int dayId) {
    if (dayId <= 0) {
      throw new IllegalArgumentException("Invalid day id " + dayId);
    }

    return companyDao.fetchCompaniesByDay(dayId);
  }

  @Override
  public List<IParticipationDto> getCompanyParticipations(int companyId) {
    if (companyId <= 0) {
      throw new IllegalArgumentException("Invalid company id " + companyId);
    }
    return participationDao.fetchParticipationsByCompany(companyId);
  }
}
