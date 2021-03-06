package paoo.cappuccino.ihm.detailscompany;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import paoo.cappuccino.business.dto.IAttendanceDto;
import paoo.cappuccino.business.dto.IBusinessDayDto;
import paoo.cappuccino.business.dto.ICompanyDto;
import paoo.cappuccino.business.dto.IContactDto;
import paoo.cappuccino.business.dto.IParticipationDto;
import paoo.cappuccino.ihm.menu.MenuEntry;
import paoo.cappuccino.ihm.menu.MenuModel;
import paoo.cappuccino.ucc.IBusinessDayUcc;
import paoo.cappuccino.ucc.ICompanyUcc;
import paoo.cappuccino.ucc.IContactUcc;
import paoo.cappuccino.ucc.IUserUcc;

public class CompanyDetailsViewController extends JPanel implements ChangeListener {

  private static final long serialVersionUID = -8984108072558274949L;
  private final JTable contactsTable;
  private final JTable participationsTable;
  private final CompanyDetailsModel model;
  private final IContactUcc contactUcc;
  private final IBusinessDayUcc dayUcc;
  private final ICompanyUcc companyUcc;
  private final CompanyDetailsView view;

  /**
   * Creates a view controller for the company details screen.
   *
   * @param model The model of the view.
   * @param menu The GUI menu model.
   * @param contactUcc App contact ucc instance.
   * @param userUcc App user ucc instance.
   */
  public CompanyDetailsViewController(CompanyDetailsModel model, MenuModel menu,
      IContactUcc contactUcc, IUserUcc userUcc, IBusinessDayUcc dayUcc, ICompanyUcc companyUcc) {
    this.model = model;
    this.contactUcc = contactUcc;
    this.companyUcc = companyUcc;
    this.dayUcc = dayUcc;

    this.view = new CompanyDetailsView(model, userUcc);
    this.add(view);

    contactsTable = view.getContactsTable();
    contactsTable.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
          menu.setCurrentPage(MenuEntry.CONTACT_DETAILS,
              contactsTable.getModel().getValueAt(contactsTable.getSelectedRow(), 0));
        }
      }
    });

    participationsTable = view.getParticipationTable();
    participationsTable.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
          IContactDto contact =
              (IContactDto) participationsTable.getModel().getValueAt(
                  participationsTable.getSelectedRow(), 2);
          if (contact == null) {
            return;
          }

          menu.setCurrentPage(MenuEntry.CONTACT_DETAILS, contact);
        }
      }
    });

    model.addChangeListener(this);
    stateChanged(null);
  }

  @Override
  public void stateChanged(ChangeEvent event) {
    ICompanyDto company = model.getCompanyDto();
    if (company == null) {
      view.stateChanged(null, null);
      return;
    }

    List<IContactDto> contacts = contactUcc.getContactByCompany(company.getId());
    List<IParticipationDto> participations = companyUcc.getCompanyParticipations(company.getId());

    DefaultTableModel contactTableModel = (DefaultTableModel) contactsTable.getModel();
    contactTableModel.setRowCount(contacts.size());
    for (int i = 0; i < contacts.size(); i++) {
      IContactDto contact = contacts.get(i);

      contactTableModel.setValueAt(contact, i, 0);
      contactTableModel.setValueAt(contact.getFirstName(), i, 1);
      contactTableModel.setValueAt(contact.isEmailValid() ? contact.getEmail() : "invalide", i, 2);
      contactTableModel.setValueAt(contact.getPhone() == null ? "N/A" : contact.getPhone(), i, 3);
    }

    DefaultTableModel participationTableModel = (DefaultTableModel) participationsTable.getModel();

    for (IParticipationDto participation : participations) {

      List<IAttendanceDto> attendanceForParticipation =
          dayUcc.getAttendancesForParticipation(participation.getBusinessDay(),
              participation.getCompany());

      int day = participation.getBusinessDay();
      IBusinessDayDto dayDto = dayUcc.getBusinessDay(day);
      participationTableModel.addRow(new Object[] {dayDto.getEventDate(), participation.getState(),
          null});

      for (IAttendanceDto attendance : attendanceForParticipation) {
        IContactDto contact = contactUcc.getContactById(attendance.getContact());
        participationTableModel.addRow(new Object[] {null, attendance.isCancelled(), contact});
      }
    }

    view.stateChanged(contacts, participations);
  }
}
