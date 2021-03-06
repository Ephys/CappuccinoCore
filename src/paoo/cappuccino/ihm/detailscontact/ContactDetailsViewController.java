package paoo.cappuccino.ihm.detailscontact;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import paoo.cappuccino.business.dto.IContactDto;
import paoo.cappuccino.ihm.core.IGuiManager;
import paoo.cappuccino.ihm.menu.MenuEntry;
import paoo.cappuccino.ihm.menu.MenuModel;
import paoo.cappuccino.ucc.ICompanyUcc;
import paoo.cappuccino.ucc.IContactUcc;

@SuppressWarnings("serial")
public class ContactDetailsViewController extends JPanel implements ChangeListener {

  private final JButton markInvalidButton = new JButton("Renseigner Invalide");
  private final ContactDetailsModel model;
  private final MenuModel menu;

  /**
   * Creates a view controller for the "contact details" screen.
   *
   * @param model The model of the view.
   * @param menu The model of the global gui menu.
   * @param guiManager The app instance of the gui manager.
   * @param contactUcc The app instance of the contact ucc.
   * @param companyUcc The app instance of the company ucc.
   */
  public ContactDetailsViewController(ContactDetailsModel model, MenuModel menu,
      IGuiManager guiManager, IContactUcc contactUcc, ICompanyUcc companyUcc) {
    this.model = model;
    this.menu = menu;

    model.addChangeListener(this);
    JButton modify = new JButton("Modifier contact");
    this.add(new ContactDetailsView(model, markInvalidButton, modify, companyUcc, menu));

    markInvalidButton.addActionListener(event -> {
      contactUcc.setMailInvalid(model.getContactDto());
      markInvalidButton.setEnabled(false);
      guiManager.getLogger().info(
          "[Contact Detail] [" + model.getContactDto().getFirstName() + " "
              + model.getContactDto().getLastName() + "] -> email invalide");
    });

    modify.addActionListener(event -> menu.setCurrentPage(MenuEntry.MODIFY_CONTACT,
                                                          model.getContactDto()));

    stateChanged(null);
  }

  @Override
  public void stateChanged(ChangeEvent event) {
    IContactDto contact = model.getContactDto();

    if (contact == null) {
      menu.setCurrentPage(MenuEntry.HOME);
      return;
    }

    markInvalidButton.setVisible(contact.getEmail() != null);
    markInvalidButton.setEnabled(contact.isEmailValid());
  }
}
