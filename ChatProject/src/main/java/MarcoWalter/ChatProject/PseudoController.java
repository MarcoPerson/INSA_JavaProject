package MarcoWalter.ChatProject;

import java.io.IOException;
import java.net.Socket;

import MarcoWalter.ChatProject.Models.OnlineUser;
import MarcoWalter.ChatProject.TcpControllers.TreadMessageSender;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class PseudoController {
	private Socket socket;
	private OnlineUser user;
	private Stage stage;

	@FXML
	Label labelPseudo;

	@FXML
	private Button changePseudoButton;

	@FXML
	private Button cancelPseudoButton;

	@FXML
	private TextField textFieldPseudo;

	public void setLabelPseudoText(String text) {
		labelPseudo.setText(text);
	}

	@FXML
	private void initialize() {

	}

	@FXML
	public void changePseudo() throws IOException {
		String pseudo = textFieldPseudo.getText();
		if (!pseudo.isEmpty()) {
			if (pseudo.equals(App.me.getPseudo())) {
				setLabelPseudoText("It is already your Pseudo !");
			} else {
				App.reception.stop();
				App.meSocketUDP.broadcast(App.me.getId(), pseudo, "Rien");
				boolean agreed = App.meSocketUDP.waitForAggrement();
				
				App.meSocketUDP.broadcast(App.me.getId(), pseudo, "Is Pseudo Ok");
				agreed = App.meSocketUDP.waitForAggrement();
				if (agreed) {
					App.meSocketUDP.broadcast(App.me.getId(), pseudo, "newPseudo");
					App.me.modifyPseudo(pseudo);
					new ControllerManager().updateHomeTitle();
					new ControllerManager().showNotification(HomeController.getInstance(), pseudo + " is your new Pseudo !");
					stage.close();

				} else {
					setLabelPseudoText("Pseudo already choosed !");
				}
				App.reception = new Thread(() -> App.meSocketUDP.receiveMessage());
				App.reception.start();
				System.out.println("New Thread working");

			}
		} else {
			setLabelPseudoText("Please write a pseudo !");
		}
	}

	@FXML
	private void cancelPseudo() throws IOException {
		stage.close();
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
