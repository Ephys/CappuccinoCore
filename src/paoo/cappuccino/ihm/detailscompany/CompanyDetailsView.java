package paoo.cappuccino.ihm.detailscompany;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import paoo.cappuccino.business.dto.ICompanyDto;
import paoo.cappuccino.business.dto.IContactDto;
import paoo.cappuccino.business.dto.IParticipationDto;
import paoo.cappuccino.business.dto.IUserDto;
import paoo.cappuccino.ihm.util.IhmConstants;
import paoo.cappuccino.ihm.util.JLabelFont;
import paoo.cappuccino.ihm.util.LocalizationUtil;
import paoo.cappuccino.ihm.util.cellrenderers.ContactCellRenderer;
import paoo.cappuccino.ihm.util.cellrenderers.ContactFullCellRenderer;
import paoo.cappuccino.ihm.util.cellrenderers.DateCellRenderer;
import paoo.cappuccino.ihm.util.cellrenderers.StateCellRenderer;
import paoo.cappuccino.ucc.IUserUcc;

public class CompanyDetailsView extends JPanel {

  private static final long serialVersionUID = 3192812307456015884L;
  private final JTable participationTable;
  private final JPanel participationList = new JPanel(new BorderLayout());
  private final JTable contactsTable;
  private final JPanel contactList = new JPanel(new BorderLayout());
  private final CompanyDetailsModel model;
  private final IUserUcc userUcc;
  private final JPanel companyPanel = new JPanel(new BorderLayout());
  private static final int MAX_SIZE_TABLE = 250;

  /**
   * Creates a view for the Company Details screen.
   *
   * @param model The model of the view.
   * @param userUcc App user use case controller.
   */
  public CompanyDetailsView(CompanyDetailsModel model, IUserUcc userUcc) {
    super(new BorderLayout());
    this.model = model;
    this.userUcc = userUcc;

    // list contacts
    String[] contactsTableTitles = new String[] {"Nom", "prenom", "Mail", "Téléphone"};
    this.contactsTable = new JTable(new DefaultTableModel(contactsTableTitles, 0) {

      private static final long serialVersionUID = -5838006548715376023L;

      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    });
    contactsTable.setRowHeight(35);
    contactsTable.getColumn(contactsTableTitles[0]).setCellRenderer(new ContactCellRenderer());

    // list participation
    String[] participationTableTitles = new String[] {"Date", "Etat", "Personne de contact"};
    this.participationTable = new JTable(new DefaultTableModel(participationTableTitles, 0) {

      private static final long serialVersionUID = -2233091768558833412L;

      @Override
      public boolean isCellEditable(int arg0, int arg1) {
        return false;
      }
    });
    participationTable.setRowHeight(35);
    TableColumn datCol = participationTable.getColumn(participationTableTitles[0]);
    datCol.setCellRenderer(new DateCellRenderer());
    datCol.setMaxWidth(datCol.getMaxWidth() / 4);

    TableColumn statCol = participationTable.getColumn(participationTableTitles[1]);
    statCol.setCellRenderer(new StateCellRenderer());
    statCol.setMaxWidth(statCol.getMaxWidth() / 4);

    participationTable.getColumn(participationTableTitles[2]).setCellRenderer(
        new ContactFullCellRenderer());


    JPanel contactWrapper = new JPanel(new BorderLayout());
    contactWrapper.add(new JLabelFont("Contacts", 17), BorderLayout.NORTH);
    contactWrapper.add(contactList, BorderLayout.CENTER);
    contactWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

    JPanel participationWrapper = new JPanel(new BorderLayout());
    participationWrapper.add(new JLabelFont("Participation", 17), BorderLayout.NORTH);
    participationWrapper.add(participationList);
    participationWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

    JPanel tables = new JPanel();
    tables.setLayout(new BoxLayout(tables, BoxLayout.Y_AXIS));
    tables.add(contactWrapper);
    tables.add(participationWrapper);

    this.add(tables);
  }

  private void displayCompanyData() {
    final ICompanyDto company = model.getCompanyDto();
    if (company == null) {
      return;
    }

    final JLabel title = new JLabelFont(company.getName(), 20);
    title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    companyPanel.add(title, BorderLayout.NORTH);

    final JPanel center = new JPanel(new BorderLayout());

    final IUserDto companyCreator = userUcc.getUserById(company.getCreator());
    final String registerText =
        "Enregistré par "
            + (companyCreator == null ? "<Supprimé>" : companyCreator.getFirstName() + " "
                + companyCreator.getLastName()) + " le "
            + LocalizationUtil.localizeDate(model.getCompanyDto().getRegisterDate());
    center.add(new JLabel(registerText), BorderLayout.NORTH);

    final JPanel addressPanel = new JPanel(new BorderLayout());
    addressPanel.add(new JLabelFont("Adresse", 17), BorderLayout.NORTH);

    final JPanel addressDataPanel = new JPanel(new GridLayout(3, 2));
    addressPanel.add(addressDataPanel);

    addressDataPanel.add(new JLabel("Rue : " + company.getAddressStreet()));
    addressDataPanel.add(new JLabel("Ville : " + company.getAddressTown()));
    addressDataPanel.add(new JLabel("Boite : " + company.getAddressMailbox()));
    addressDataPanel.add(new JLabel("Numero : "
        + (company.getAddressNum() == null ? "N/A" : company.getAddressNum())));
    addressDataPanel.add(new JLabel("Code postal : " + company.getAddressPostcode()));

    companyPanel.add(addressPanel);

    this.add(companyPanel, BorderLayout.NORTH);
  }

  JTable getContactsTable() {
    return contactsTable;
  }

  JTable getParticipationTable() {
    return participationTable;
  }

  /**
   * Called when the data to display changes.
   *
   * @param contacts The list of contacts of the company.
   */
  void stateChanged(List<IContactDto> contacts, List<IParticipationDto> participations) {
    companyPanel.removeAll();
    displayCompanyData();

    contactList.removeAll();
    if (contacts != null) {
      if (contacts.size() == 0) {
        JLabel errorLabel = new JLabel(IhmConstants.ERROR_NO_CONTACTS);
        errorLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        contactList.add(errorLabel);
      } else {
        JScrollPane scrollContact = new JScrollPane(contactsTable);
        int preferedSize = contacts.size() * contactsTable.getRowHeight() + 28;
        scrollContact.setPreferredSize(new Dimension(650,
            preferedSize > MAX_SIZE_TABLE ? MAX_SIZE_TABLE : preferedSize));
        contactList.add(scrollContact);
      }
    }

    participationList.removeAll();
    if (participations == null || participations.size() == 0) {
      JLabel errorLabel = new JLabel(IhmConstants.ERROR_NO_PARTICIPATIONS);
      errorLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
      participationList.add(errorLabel);
    } else {
      JScrollPane scrollParticipations = new JScrollPane(participationTable);
      int preferedSize =
          (participationTable.getRowCount() * participationTable.getRowHeight()) + 28;
      scrollParticipations.setPreferredSize(new Dimension(650,
          preferedSize > MAX_SIZE_TABLE ? MAX_SIZE_TABLE : preferedSize));
      participationList.add(scrollParticipations);
    }
  }
}
