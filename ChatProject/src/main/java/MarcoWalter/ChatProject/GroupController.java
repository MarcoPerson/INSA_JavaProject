package MarcoWalter.ChatProject;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import MarcoWalter.ChatProject.Models.OnlineUser;
import MarcoWalter.ChatProject.Models.User;
import MarcoWalter.ChatProject.Models.UserBookManager;
import MarcoWalter.ChatProject.TcpControllers.TreadMessageSender;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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

public class GroupController {
	private Socket socket;
	private OnlineUser user;
	private Stage stage;
	
	@FXML
	Label chatGroupInfoField;
	@FXML
	private TextField textFieldGroup;
	@FXML
	private VBox listGroupUserVbox;
	
	private HashMap<String, OnlineUser> pseudoMap = new HashMap<String, OnlineUser>();

	@FXML
	private void initialize() {
		
	}
	
	public void initializeUsers(UserBookManager book) {
		for(OnlineUser user: book.getListOfOnlineUser()) {
			pseudoMap.put(user.getPseudo(), user);
			addUserToGroup(user.getPseudo());
		}
	}
	
	public void setChatGroupInfoFieldText(String text) {
		System.out.println(text);
		chatGroupInfoField.setText(text);
	}

	@FXML
	public void addUserToGroup(String pseudo) {
		HBox hBox = new HBox();
		CheckBox checkBox = new CheckBox();
		checkBox.setText(pseudo);
		hBox.getChildren().add(checkBox);
		listGroupUserVbox.getChildren().add(hBox);
	}

	@FXML
	public void createGroup() {
		ArrayList<OnlineUser> listOfSelectedMembers = new ArrayList<OnlineUser>();
		String groupName = textFieldGroup.getText();
		if(!groupName.trim().isEmpty()) {
			List<String> selectedUsers = new ArrayList<String>();
			for (int i = 0; i < listGroupUserVbox.getChildren().size() - 1; i++) {
				HBox hbox = (HBox) listGroupUserVbox.getChildren().get(i+1);
				System.out.println("Group : " + hbox.getChildren().size());
				CheckBox checkBox = (CheckBox) hbox.getChildren().get(0);
				if (checkBox.isSelected()) {
					selectedUsers.add(checkBox.getText());
					listOfSelectedMembers.add(pseudoMap.get(checkBox.getText()));
				}
			}
			System.out.println("Group Name: " + groupName);
			System.out.println("Selected Users: " + selectedUsers);
			App.me.createChatGroup(groupName, listOfSelectedMembers, App.meSocketUDP);
			stage.close();
		}else {
			setChatGroupInfoFieldText("Please enter a valid group name !");
		}
		
	}

	@FXML
	
	private void cancelGroup() throws IOException {
		stage.close();
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
