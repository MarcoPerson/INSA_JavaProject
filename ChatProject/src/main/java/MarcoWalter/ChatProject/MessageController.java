package MarcoWalter.ChatProject;

import java.io.IOException;
import java.net.Socket;

import MarcoWalter.ChatProject.Models.OnlineUser;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class MessageController {
	private Socket socket;
	private OnlineUser user;
	
	@FXML 
	Label userPseudo;
	
	public void setuserPseudoText(String text) {
		System.out.println(text);
		userPseudo.setText(text);
	}
	
	@FXML
	private void initialize() {
		// App.getStage().setTitle("Login");
	}
	
    @FXML
    private void sendMessage() throws IOException {
    	
    }

	public OnlineUser getUser() {
		return user;
	}

	public void setUser(OnlineUser user) {
		this.user = user;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}
