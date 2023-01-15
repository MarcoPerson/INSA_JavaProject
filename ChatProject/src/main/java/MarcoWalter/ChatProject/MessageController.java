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

public class MessageController {
	private Socket socket;
	private OnlineUser user;
	
	@FXML 
	Label userPseudo;
	
	@FXML 
	private Button sendMessage;
	
	@FXML
	private ScrollPane scrollMessage;
	
	@FXML
	private VBox messageBox;
	
	@FXML
	private TextArea messageToSendField;
	
	public void setUserPseudoText(String text) {
		userPseudo.setText(text);
	}
	
	@FXML
	private void initialize() {
		messageBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            	scrollMessage.setVvalue((Double) newValue);
            }
        });
	}
	
    @FXML
    private void sendMessage() throws IOException {
    	String message = messageToSendField.getText();
    	if(!message.isEmpty()) {
    		new TreadMessageSender(user, socket, message);
    		addSenderMessage(message);
    		messageToSendField.clear();
    	}
    }
    
    public void addReceiverMessage(String message) {
    	HBox hbox1 = new HBox();
        hbox1.setAlignment(Pos.CENTER_LEFT);

        VBox.setMargin(hbox1, new Insets(0, 0, 2, 0));

        TextFlow textFlow1 = new TextFlow();
        textFlow1.setPrefHeight(20);
        textFlow1.setPrefWidth(200);
        textFlow1.setStyle("-fx-background-color: #39E75F; -fx-background-radius: 5px;");

        Text text1 = new Text(message);
        textFlow1.getChildren().add(text1);

        HBox.setMargin(textFlow1, new Insets(0, 0, 2, 0));
        textFlow1.setPadding(new Insets(5, 5, 5, 5));

        hbox1.getChildren().add(textFlow1);
        
        messageBox.getChildren().add(hbox1);
	}
    
    private void addSenderMessage(String message) {
    	HBox hbox2 = new HBox();
        hbox2.setAlignment(Pos.CENTER_RIGHT);

        TextFlow textFlow2 = new TextFlow();
        textFlow2.setPrefHeight(20);
        textFlow2.setPrefWidth(200);
        textFlow2.setStyle("-fx-background-color: #45B6FE; -fx-background-radius: 5px;");

        Text text2 = new Text(message);
        textFlow2.getChildren().add(text2);

        textFlow2.setPadding(new Insets(5, 5, 5, 5));

        hbox2.getChildren().add(textFlow2);
        VBox.setMargin(hbox2, new Insets(0, 0, 2, 0));
        
        messageBox.getChildren().add(hbox2);
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

	public void updateOnlineUser(OnlineUser user) {
		setUser(user);
		setUserPseudoText(user.getPseudo());
	}
}
