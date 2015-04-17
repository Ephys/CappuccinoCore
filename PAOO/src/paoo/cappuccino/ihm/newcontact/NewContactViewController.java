package paoo.cappuccino.ihm.newcontact;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import paoo.cappuccino.business.dto.ICompanyDto;
import paoo.cappuccino.business.dto.IContactDto;
import paoo.cappuccino.ihm.core.IGuiManager;
import paoo.cappuccino.ihm.util.CompanyListRenderer;
import paoo.cappuccino.ihm.util.IhmConstants;
import paoo.cappuccino.ucc.ICompanyUcc;
import paoo.cappuccino.ucc.IContactUcc;
import paoo.cappuccino.util.StringUtils;

/**
 * ViewController for the new contact Gui.
 *
 * @author Opsomer Mathias
 */
@SuppressWarnings("serial")
public class NewContactViewController extends JPanel {

  /**
   * Creates a new ViewController for the new contact gui.
   *
   * @param model The ViewController's model.
   */
  public NewContactViewController(NewContactModel model, IGuiManager manager,
                                  IContactUcc contactUcc, ICompanyUcc companyUcc) {
    super(new BorderLayout());
    this.setBorder(new EmptyBorder(IhmConstants.L_GAP, IhmConstants.M_GAP, 0, IhmConstants.M_GAP));

    // log
    manager.getLogger().info("NewContact Frame");

    JTextField contactFirstNameField = new JTextField();
    JTextField contactLastNameField = new JTextField();
    JTextField contactMailField = new JTextField();
    JTextField contactPhoneField = new JTextField();

    List<ICompanyDto> allCompanies = companyUcc.getAllCompanies();
    JComboBox<ICompanyDto> comboCompanies =
        new JComboBox<>(allCompanies.toArray(new ICompanyDto[allCompanies.size()]));
    comboCompanies.setRenderer(new CompanyListRenderer());

    // if (menu.hasTransitionObject())
    //   comboCompanies.setSelectedItem(menu.getTransitionObject());

    JPanel controls =
        new JPanel(new FlowLayout(FlowLayout.RIGHT, IhmConstants.M_GAP, IhmConstants.M_GAP));

    JButton createButton = new JButton("Créer");
    createButton
        .addActionListener(e -> {
          // test input
          model.setFirstNameError(
              StringUtils.isEmpty(contactFirstNameField.getText()) ? IhmConstants.ERROR_FIELD_EMPTY
                                                                   : (
                  !StringUtils.isAlphaString(contactFirstNameField.getText())
                  ? IhmConstants.ERROR_ALPHA_INPUT
                  : null));

          model.setLastNameError(
              StringUtils.isEmpty(contactLastNameField.getText()) ? IhmConstants.ERROR_FIELD_EMPTY
                                                                  : (
                  !StringUtils.isAlphaString(contactLastNameField.getText())
                  ? IhmConstants.ERROR_ALPHA_INPUT
                  : null));

          model.setMailError(
              StringUtils.isEmpty(contactMailField.getText()) ? IhmConstants.ERROR_FIELD_EMPTY
                                                              : (
                  !StringUtils.isEmail(contactMailField.getText())
                  ? IhmConstants.ERROR_INVALID_EMAIL
                  : null));

          model.setPhoneError(
              StringUtils.isEmpty(contactPhoneField.getText()) ? IhmConstants.ERROR_FIELD_EMPTY
                                                               : null);

          if (!model.hasError()) {
            IContactDto contact =
                contactUcc.create(((ICompanyDto) comboCompanies.getSelectedItem()).getId(),
                                  contactMailField.getText(), contactFirstNameField.getText(),
                                  contactLastNameField.getText(), contactPhoneField.getText());

            if (contact != null) {
              model.clearError();
              manager.getLogger().info(
                  "new Contact created : " + contact.getFirstName() + " " + contact.getLastName()
                  + "  ( " + ((ICompanyDto) comboCompanies.getSelectedItem()).getName() + " )");
              JOptionPane.showMessageDialog(null, "Contact créer");

              // clear les champs
              contactFirstNameField.setText(null);
              contactLastNameField.setText(null);
              contactMailField.setText(null);
              contactPhoneField.setText(null);
            } else {
              JOptionPane.showMessageDialog(this, "Erreure survenue lors de la création du contact."
                                                  + " Veuillez réessayer.");
            }
          }

        });

    controls.add(createButton);

    this.add(controls, BorderLayout.SOUTH);
    // end buttons //

    this.add(new NewContactView(model, contactFirstNameField, contactLastNameField,
                                contactMailField, contactPhoneField, comboCompanies));
  }
}
