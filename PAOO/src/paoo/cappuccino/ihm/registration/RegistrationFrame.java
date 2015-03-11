package paoo.cappuccino.ihm.registration;

import paoo.cappuccino.core.injector.Inject;
import paoo.cappuccino.ihm.core.IGuiManager;
import paoo.cappuccino.ihm.login.LoginModel;
import paoo.cappuccino.ihm.login.LoginViewController;
import paoo.cappuccino.ihm.util.BaseFrame;
import paoo.cappuccino.ucc.IUserUcc;
/**
 * Frame for the registration Gui.
 *
 * @author Maduka Junior
 */
public class RegistrationFrame extends BaseFrame{

	  /**
	   * Creates a new frame for the registration gui.
	   *
	   * @param userUcc User use case controller.
	   * @param guiManager The manager responsible for opening/closing this frame.
	   */
	  @Inject
	  public RegistrationFrame(IUserUcc userUcc, IGuiManager guiManager) {
	    super("S'inscrire", 550, 600);
	    this.setResizable(false);

	    this.add(new RegistrationViewController(new RegistrationModel(userUcc), guiManager));
	    this.setVisible(true);
	  }
}