package paoo.cappuccino.ihm.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import paoo.cappuccino.business.dto.IUserDto;
import paoo.cappuccino.ihm.core.IGuiManager;
import paoo.cappuccino.ihm.menu.MenuFrame;
import paoo.cappuccino.ihm.registration.RegistrationFrame;
import paoo.cappuccino.ihm.util.IhmConstants;
import paoo.cappuccino.util.StringUtils;

/**
 * ViewController for the Login Gui.
 *
 * @author Opsomer Mathias
 */
public class LoginViewController extends JPanel {

  private static final long serialVersionUID = 3071496812344175953L;
  private final LoginModel model;
  private final IGuiManager manager;

  /**
   * Creates a new ViewController for the Login gui.
   *
   * @param model The ViewController's model.
   * @param manager The manager responsible for the containing frame.
   */
  public LoginViewController(LoginModel model, IGuiManager manager) {
    super(new BorderLayout());
    this.model = model;
    this.manager = manager;
   
    this.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(80,80,80)),
        new EmptyBorder(IhmConstants.L_GAP, IhmConstants.M_GAP, 0, IhmConstants.M_GAP)));

    JTextField usernameField = new JTextField();
    JPasswordField passwordField = new JPasswordField();

    // buttons //
    JPanel controls = new JPanel(new GridLayout(2, 0, 0, IhmConstants.M_GAP));

    controls.setLayout(new FlowLayout(FlowLayout.RIGHT, IhmConstants.M_GAP, IhmConstants.M_GAP));

    JButton registerButton = new JButton("S'inscrire");
    registerButton.addActionListener(e -> manager.openFrame(RegistrationFrame.class));

    JButton loginButton = new JButton("Se connecter");
    loginButton.addActionListener(e -> {
      login(usernameField, passwordField);
    });

    controls.add(registerButton);
    controls.add(loginButton);

    this.add(controls, BorderLayout.SOUTH);
    // end buttons //

    this.add(new LoginView(model, usernameField, passwordField));
  }

  private void login(JTextField usernameField, JPasswordField passwordField) {
    IUserDto user = attemptLogin(usernameField.getText(), passwordField.getPassword());
    if (user != null) {
      manager.openFrame(MenuFrame.class).setLoggedUser(user);
    }

  }

  /**
   * Tries to log the user in.
   *
   * @param username The user's username.
   * @param password The user's password.
   * @return the user logged in or null
   */
  private IUserDto attemptLogin(String username, char[] password) {
    model.resetErrors();

    boolean isValid = true;
    if (StringUtils.isEmpty(username)) {
      model.setUsernameError(IhmConstants.ERROR_FIELD_EMPTY);
      isValid = false;
    }

    if (password.length == 0) {
      model.setPasswordError(IhmConstants.ERROR_FIELD_EMPTY);
      isValid = false;
    }

    if (!isValid) {
      return null;
    }

    IUserDto user = model.getUserUcc().logIn(username, password);

    if (user == null) {
      model.setUsernameError(IhmConstants.ERROR_WRONG_LOGIN);
      model.setPasswordError(IhmConstants.ERROR_WRONG_LOGIN);
    } else {
      // avoid password release in case of memory dump.
      StringUtils.clearString(password);
    }

    return user;
  }
}
