package paoo.cappuccino.dal.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import paoo.cappuccino.business.dto.IAttendanceDto;
import paoo.cappuccino.business.entity.IAttendance;
import paoo.cappuccino.business.entity.factory.IEntityFactory;
import paoo.cappuccino.core.injector.Inject;
import paoo.cappuccino.dal.dao.IAttendanceDao;

public class MockAttendanceDao implements IAttendanceDao {

  private final List<IAttendance> attendances = new ArrayList<>();
  private final IEntityFactory factory;

  @Inject
  public MockAttendanceDao(IEntityFactory entityFactory) {
    this.factory = entityFactory;
  }

  @Override
  public IAttendanceDto createAttendance(IAttendanceDto attendance) {
    IAttendance attendanceEntity = factory.createAttendance(attendance.getCompany(),
                                                            attendance.getBusinessDay(),
                                                            attendance.getContact());

    attendances.add(attendanceEntity);

    return attendanceEntity;
  }

  @Override
  public List<IAttendanceDto> fetchAttendances(int companyId, int businessDayId) {
    return attendances.stream()
        .filter(attendance -> attendance.getCompany() == companyId
                              && attendance.getBusinessDay() == businessDayId)
        .collect(Collectors.toList());
  }
}